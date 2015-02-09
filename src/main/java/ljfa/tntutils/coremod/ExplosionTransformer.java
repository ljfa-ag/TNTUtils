package ljfa.tntutils.coremod;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import ljfa.tntutils.util.LogHelper;
import net.minecraft.launchwrapper.IClassTransformer;

public class ExplosionTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        FMLRelaunchLog.log("TntUtils-Core", Level.INFO, "About to transform class %s", name);
        return basicClass;
    }

}
