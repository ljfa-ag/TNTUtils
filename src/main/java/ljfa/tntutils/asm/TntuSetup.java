package ljfa.tntutils.asm;

import java.io.File;
import java.util.Map;

import ljfa.tntutils.Config;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.IFMLCallHook;

public class TntuSetup implements IFMLCallHook {

    @Override
    public Void call() throws Exception {
        FMLRelaunchLog.log("TNTUtils Core", Level.INFO, "Loading configuration");
        Config.loadConfig(new File("config/tnt_utilities.cfg"));
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

}
