package ljfa.tntutils.coremod;

import ljfa.tntutils.util.LogHelper;
import net.minecraft.launchwrapper.IClassTransformer;

public class ExplosionTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        LogHelper.info("About to transform class %s", name);
        return basicClass;
    }

}
