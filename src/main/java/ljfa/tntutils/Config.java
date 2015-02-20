package ljfa.tntutils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import ljfa.tntutils.exception.InvalidConfigValueException;
import ljfa.tntutils.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config {
    public static Configuration conf;

    public static final String CATEGORY_GENERAL = "general";
    
    public static boolean replaceTNT;
    public static boolean explosionCommand;
    public static boolean spareTileEntities;
    public static String[] blacklistArray;
    public static Set<Block> blacklist = null;
    public static boolean blacklistActive = false;
    public static boolean disableBlockDamage;
    public static boolean disableCreeperBlockDamage;
    public static boolean disableEntityDamage;
    public static boolean disableNPCDamage;
    public static boolean disableExplosions;
    public static float dropChanceModifier;
    
    public static void loadConfig(File file) {
        if(conf == null)
            conf = new Configuration(file);
        
        conf.load();
        loadValues();
        
        FMLCommonHandler.instance().bus().register(new ChangeHandler());
    }
    
    public static void loadValues() {
        replaceTNT = conf.get(CATEGORY_GENERAL, "preventChainExplosions", false, "Prevents explosions from triggering TNT and thus disables chain explosions").setRequiresMcRestart(true).getBoolean();
        explosionCommand = conf.get(CATEGORY_GENERAL, "addExplosionCommand", true, "Adds the \"/explosion\" command").setRequiresMcRestart(true).getBoolean();
        spareTileEntities = conf.get(CATEGORY_GENERAL, "disableTileEntityDamage", false, "Makes explosions not destroy tile entities").getBoolean();
        blacklistArray = conf.get(CATEGORY_GENERAL, "destructionBlacklist", new String[0], "A list of blocks that will never be destroyed by explosions").getStringList();
        disableCreeperBlockDamage = conf.get(CATEGORY_GENERAL, "disableCreeperBlockDamage", false, "\"Environmentally Friendly Creepers\": Makes creepers not destroy blocks").getBoolean();
        disableBlockDamage = conf.get(CATEGORY_GENERAL, "disableBlockDamage", false, "Disables all block damage from explosions").getBoolean();
        disableEntityDamage = conf.get(CATEGORY_GENERAL, "disableEntityDamage", false, "Disables all entity damage from explosions").getBoolean();
        disableNPCDamage = conf.get(CATEGORY_GENERAL, "disableNPCDamage", false, "No livings besides players get damage from explosions").getBoolean();
        disableExplosions = conf.get(CATEGORY_GENERAL, "disableExplosions", false, "Entirely disables all effects from explosions").getBoolean();
        dropChanceModifier = (float)conf.get(CATEGORY_GENERAL, "dropChanceIncrease", 0.0, "Increases the chance that explosions will drop destroyed blocks as items\n"
                + "0 = Vanilla behavior, 1 = always drop items.\n"
                + "This option will only affect explosions of size <= 7 because a large number of dropped items can cause lag.", 0.0, 1.0).getDouble();
        //----------------
        if(conf.hasChanged())
            conf.save();
    }
    
    public static void createBlacklistSet() {
        blacklist = new HashSet<Block>();
        for(String name: blacklistArray) {
            Block block = (Block)Block.blockRegistry.getObject(name);
            if(block == Blocks.air || block == null) {
                throw new InvalidConfigValueException("Invalid blacklist entry: " + name);
            } else {
                blacklist.add(block);
                LogHelper.debug("Added block to blacklist: %s", name);
            }
        }
        blacklistActive = blacklist.size() != 0;
    }
    
    /** Reloads the config values upon change */
    public static class ChangeHandler {
        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.modID.equals(Reference.MODID)) {
                loadValues();
                createBlacklistSet();
            }
        }
    }
}
