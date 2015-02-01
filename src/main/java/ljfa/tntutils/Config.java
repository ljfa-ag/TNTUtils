package ljfa.tntutils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.common.config.Configuration;
import scala.actors.threadpool.Arrays;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config {
    public static Configuration conf;

    public static final String CATEGORY_GENERAL = "general";
    
    public static boolean replaceTNT;
    public static boolean explosionCommand;
    public static boolean spareTileEntities;
    public static Set<String> blacklist;
    public static boolean blacklistActive;
    
    public static void loadConfig(File file) {
        if(conf == null)
            conf = new Configuration(file);
        
        conf.load();
        loadValues();
        
        FMLCommonHandler.instance().bus().register(new ChangeHandler());
    }
    
    public static void loadValues() {
        replaceTNT = conf.get(CATEGORY_GENERAL, "preventChainExplosions", true, "Prevent explosions from triggering TNT").setRequiresMcRestart(true).getBoolean();
        explosionCommand = conf.get(CATEGORY_GENERAL, "addExplosionCommand", true, "Adds the \"/explosion\" command").setRequiresMcRestart(true).getBoolean();
        spareTileEntities = conf.get(CATEGORY_GENERAL, "spareTileEntites", false, "Makes explosions not destroy tile entities").getBoolean();
        blacklist = processBlacklist(conf.get(CATEGORY_GENERAL, "destructionBlacklist", new String[0], "A list of blocks that will never be destroyed by explosions").getStringList());
        blacklistActive = blacklist.size() != 0;
        //----------------
        if(conf.hasChanged())
            conf.save();
    }
    
    private static Set<String> processBlacklist(String[] array) {
        Set<String> set = new HashSet<String>(2 * array.length);
        for(String name: array)
            set.add("tile." + name);
        return set;
    }
    
    /** Reloads the config values upon change */
    public static class ChangeHandler {
        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.modID.equals(Reference.MODID))
                loadValues();
        }
    }
}
