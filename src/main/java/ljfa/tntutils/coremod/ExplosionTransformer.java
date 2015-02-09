package ljfa.tntutils.coremod;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class ExplosionTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        FMLRelaunchLog.log("TNTUtils Core", Level.INFO, "Name: %s, Transformed: %s", name, transformedName);
        return basicClass;
    }
    
    private byte[] patchClassASM(String name, byte[] basicClass) {
      //ASM manipulation stuff
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        
        for(MethodNode mn: classNode.methods) {
            patchMethod(mn);
        }
        
        //Write class
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private void patchMethod(MethodNode mn) {
        //Loop through the instructions of the method
        Iterator<AbstractInsnNode> it = mn.instructions.iterator();
        while(it.hasNext()) {
            AbstractInsnNode currentNode = it.next();
            
            if(currentNode.getOpcode() == Opcodes.NEW) {
                FMLRelaunchLog.log("TNTUtils Core", Level.INFO, "%s", ((TypeInsnNode)currentNode).desc);
            }
        }
    }
}
