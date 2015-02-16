package ljfa.tntutils.asm;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

/** Currently not working */
public class ExplConstrTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.startsWith("net.minecraft.") || transformedName.startsWith("net.minecraftforge."))
            return patchClassASM(name, basicClass);
        else
            return basicClass;
    }
    
    private byte[] patchClassASM(String name, byte[] basicClass) {
      //ASM manipulation stuff
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        
        boolean didPatch = false;
        for(MethodNode mn: classNode.methods) {
            didPatch = patchMethod(mn) || didPatch;
        }
        
        if(didPatch) {
            //Write class
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(writer);
            return writer.toByteArray();
        } else
            return basicClass;
    }

    private boolean patchMethod(MethodNode mn) {
        boolean didPatch = false;
        //Loop through the instructions of the method
        Iterator<AbstractInsnNode> it = mn.instructions.iterator();
        while(it.hasNext()) {
            AbstractInsnNode currentNode = it.next();
            
            if(currentNode.getOpcode() == Opcodes.NEW) {
                FMLRelaunchLog.log("TNTUtils Core", Level.INFO, "new %s", ((TypeInsnNode)currentNode).desc);
                didPatch = true;
            }
            else if(currentNode.getOpcode() == Opcodes.INVOKESPECIAL) {
                MethodInsnNode invokespecialNode = (MethodInsnNode)currentNode;
                FMLRelaunchLog.log("TNTUtils Core", Level.INFO, "Special method %s%s from %s",
                        invokespecialNode.name, invokespecialNode.desc, invokespecialNode.owner);
                didPatch = true;
            }
        }
        return didPatch;
    }
}
