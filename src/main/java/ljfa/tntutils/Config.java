package ljfa.tntutils;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config {
    public static Configuration conf;

    public static final String CATEGORY_GENERAL = "general";
    
    public static boolean replaceTNT;
    public static boolean explosionCommand;
    
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
