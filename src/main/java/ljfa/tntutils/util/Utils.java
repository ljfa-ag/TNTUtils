package ljfa.tntutils.util;

import net.minecraft.launchwrapper.Launch;

public class Utils {
    public static final boolean deobfuscatedEnv = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
}
