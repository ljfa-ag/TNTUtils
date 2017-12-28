package ljfa.tntutils.asm;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class ExplosionTransformer implements IClassTransformer {
    private final Logger coreLogger = LogManager.getLogger("TNTUtils Core", StringFormatterMessageFactory.INSTANCE);
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.equals("net.minecraft.world.Explosion")) {
            if(name.equals(transformedName)) {
                coreLogger.info("About to patch class %s", transformedName);
                return patchClassExplosion(basicClass, false);
            }
            else {
                coreLogger.info("About to patch obfuscated class %s (%s)", name, transformedName);
                return patchClassExplosion(basicClass, true);
            }
        } else
            return basicClass;
    }
    
    private byte[] patchClassExplosion(byte[] basicClass, boolean obfuscated) {
        //ASM manipulation stuff
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        //Loop through the methods until we find our target
        for(MethodNode mn: classNode.methods) {
            if(mn.name.equals(obfuscated ? "func_77279_a" : "doExplosionB") && mn.desc.equals("(Z)V")) {
                coreLogger.trace("Found target method %s%s", mn.name, mn.desc);
                patchDoExplosionB(mn);
                break;
            }
        }

        //Write class
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
    
    private void patchDoExplosionB(MethodNode mn) {
        //Loop through the instructions of the method
        Iterator<AbstractInsnNode> it = mn.instructions.iterator();
        boolean didInject = false;
        while(it.hasNext()) {
            AbstractInsnNode currentNode = it.next();
            /* In the Explosion class, line 239:
             * 
             * Currently, the division 1.0F / this.explosionSize for the drop chance is being performed.
             * We want to get rid of this and instead call a hook method that calculates the chance for us.
             * This hook method would be HooksExplosion.getDropChance().
             * 
             * So we search for the sequence fconst_1, aload_0, getfield, fdiv.
             * We insert a "goto" instruction to skip over it and instead insert an "invokestatic" to call our hook.
             */
            //Search for "fdiv"
            if(currentNode.getOpcode() == Opcodes.FDIV) {
                InsnNode fdivNode = (InsnNode)currentNode;
                //Go one step back
                currentNode = currentNode.getPrevious();
                //Check if preceding instruction is "getfield"
                if(currentNode.getOpcode() == Opcodes.GETFIELD) {            
                    //Go two steps back
                    currentNode = currentNode.getPrevious().getPrevious();
                    //Here should be a "fconst_1"
                    if(currentNode.getOpcode() == Opcodes.FCONST_1) {
                        coreLogger.trace("Found target instructions \"fconst_1\" through \"fdiv\"");
                        
                        //Insert a label after "fdiv"
                        LabelNode label = new LabelNode();
                        mn.instructions.insert(fdivNode, label);
                        
                        //Insert a hook call and a "goto" instruction before fconst_1
                        InsnList toInject = new InsnList();
                        toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(HooksExplosion.class), "getDropChance", "(Lnet/minecraft/world/Explosion;)F", false));
                        toInject.add(new JumpInsnNode(Opcodes.GOTO, label));
                        
                        mn.instructions.insertBefore(currentNode, toInject);
                        
                        didInject = true;
                        break;
                    }
                }
            }
        }
        if(didInject)
            coreLogger.info("Successfully injected into %s%s", mn.name, mn.desc);
        else
            coreLogger.error("Failed injection into %s%s. There is probably an incompatibility with some other core mod.", mn.name, mn.desc);
    }
}
