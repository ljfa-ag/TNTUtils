package ljfa.tntutils.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.common.FMLLog;

public class ExplosionTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(name.equals("net.minecraft.world.Explosion")) {
            FMLLog.log("TNTUtils Core", Level.INFO, "About to patch class %s", name);
            return patchClassASM(name, basicClass, false);
        } else if(name.equals("agw")) {
            FMLLog.log("TNTUtils Core", Level.INFO, "About to patch obfuscated class %s", name);
            return patchClassASM(name, basicClass, true);
        } else
            return basicClass;
    }
    
    private byte[] patchClassASM(String name, byte[] basicClass, boolean obfuscated) {
        //ASM manipulation stuff
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        //Loop through the methods until we find our target
        for(MethodNode mn: classNode.methods) {
            if(mn.name.equals(obfuscated ? "func_77279_a" : "doExplosionB") && mn.desc.equals("(Z)V")) {
                FMLLog.log("TNTUtils Core", Level.INFO, "Found target method %s%s", mn.name, mn.desc);
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
        
    }
}
