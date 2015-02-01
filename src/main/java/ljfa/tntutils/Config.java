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
    
    public static void loadConfig(File file) {
        if(conf == null)
            conf = new Configuration(file);
        
        conf.load();
        loadValues();
        
        FMLCommonHandler.instance().bus().register(new ChangeHandler());
    }
    
    public static void loadValues() {
        replaceTNT = conf.getBoolean("preventChainExplosions", CATEGORY_GENERAL, true, "Prevent explosions from triggering TNT");
        explosionCommand = conf.getBoolean("addExplosionCommand", CATEGORY_GENERAL, true, "Adds the \"/explosion\" command");
        spareTileEntities = conf.getBoolean("spareTileEntites", CATEGORY_GENERAL, false, "Makes explosions not destroy tile entities");
        blacklist = new HashSet<String>(Arrays.asList(
            conf.getStringList("destructionBlacklist", CATEGORY_GENERAL, new String[0], "A list of blocks that will never be destroyed by explosions")
            ));
        //----------------
        if(conf.hasChanged())
            conf.save();
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
