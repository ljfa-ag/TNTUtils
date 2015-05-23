package ljfa.tntutils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import ljfa.tntutils.exception.InvalidConfigValueException;
import ljfa.tntutils.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {
    public static Configuration conf;

    public static final String CAT_GENERAL = "general";
    public static final String CAT_BLOCKDMG = "block_damage";
    public static final String CAT_ENTDMG = "entity_damage";
    
    public static boolean explosionCommand;
    public static float sizeMultiplier;
    public static float dropChanceModifier;
    public static boolean disableExplosions;
    public static boolean preventChainExpl;
    public static boolean disableTNT;
    public static boolean disableTNTMinecart;
    
    public static boolean disableBlockDamage;
    public static boolean disableCreeperBlockDamage;
    public static Set<Block> blacklist;
    public static boolean blacklistActive;
    public static boolean spareTileEntities;
    
    public static boolean disableEntityDamage;
    public static boolean disablePlayerDamage;
    public static boolean disableNPCDamage;
    
    public static void loadConfig(File file) {
        if(conf == null)
            conf = new Configuration(file);
        
        conf.load();
        loadValues();
        
        FMLCommonHandler.instance().bus().register(new ChangeHandler());
    }
    
    public static void loadValues() {
        explosionCommand = conf.get(CAT_GENERAL, "addExplosionCommand", true, "Adds the \"/explosion\" command").setRequiresMcRestart(true).getBoolean();
        sizeMultiplier = (float)conf.get(CAT_GENERAL, "sizeMultiplier", 1.0, "Multiplies the size of all explosions by this", 0.0, 50.0).getDouble();
        dropChanceModifier = (float)conf.get(CAT_GENERAL, "dropChanceIncrease", 0.0, "Increases the chance that explosions will drop destroyed blocks as items\n"
                + "0 = Vanilla behavior, 1 = always drop items.\n"
                + "Only affects explosions of size <= 10 since a large number of dropped items can cause lag.", 0.0, 1.0).getDouble();
        disableExplosions = conf.get(CAT_GENERAL, "disableExplosions", false, "Entirely disables all effects from explosions").getBoolean();
        preventChainExpl = conf.get(CAT_GENERAL, "preventChainExplosions", false, "Prevents explosions from triggering TNT and thus disables chain explosions").setRequiresMcRestart(true).getBoolean();
        disableTNT = conf.get(CAT_GENERAL, "disableTNT", false, "Disables TNT explosions").setRequiresMcRestart(true).getBoolean();
        disableTNTMinecart = conf.get(CAT_GENERAL, "disableTNTMinecart", false, "Disables the placement of TNT minecarts").setRequiresMcRestart(true).getBoolean();
        //----------------
        //destructionBlacklist is being read in createBlacklistSet()
        disableBlockDamage = conf.get(CAT_BLOCKDMG, "disableBlockDamage", false, "Disables all block damage from explosions").getBoolean();
        disableCreeperBlockDamage = conf.get(CAT_BLOCKDMG, "disableCreeperBlockDamage", false, "\"Environmentally Friendly Creepers\": Makes creepers not destroy blocks").getBoolean();
        spareTileEntities = conf.get(CAT_BLOCKDMG, "disableTileEntityDamage", false, "Makes explosions not destroy tile entities").getBoolean();
        //----------------
        disableEntityDamage = conf.get(CAT_ENTDMG, "disableEntityDamage", false, "Disables explosion damage to all entities (also includes items, minecarts etc.)").getBoolean();
        disablePlayerDamage = conf.get(CAT_ENTDMG, "disablePlayerDamage", false, "Disables explosion damage to players").getBoolean();
        disableNPCDamage = conf.get(CAT_ENTDMG, "disableNPCDamage", false, "Disables explosion damage to animals and mobs").getBoolean();
        //----------------
    }
    
    public static void createBlacklistSet() {
        String[] blacklistArray = conf.get(CAT_BLOCKDMG, "destructionBlacklist", new String[0], "A list of blocks that will never be destroyed by explosions").getStringList();
        
        blacklist = new HashSet<Block>();
        for(String name: blacklistArray) {
            Block block = (Block)Block.blockRegistry.getObject(name);
            if(block == Blocks.air || block == null)
                throw new InvalidConfigValueException("destructionBlacklist: Invalid block name: " + name);
            
            blacklist.add(block);
            LogHelper.debug("Added block to blacklist: %s", name);
        }
        blacklistActive = blacklist.size() != 0;
    }
    
    public static void modifyResistances() {
        String[] entries = conf.get(CAT_BLOCKDMG, "blockResistances", new String[0], "Change the explosion resistance of individual blocks.\n"
                + "Syntax: modid:block=value").setRequiresMcRestart(true).getStringList();
        
        for(String str: entries) {
            int ind = str.indexOf('=');
            if(ind == -1)
                throw new InvalidConfigValueException("blockResistances: Syntax error: " + str);
            String blockName = str.substring(0, ind);
            String valueStr = str.substring(ind+1);
            
            Block block = (Block)Block.blockRegistry.getObject(blockName);
            if(block == Blocks.air || block == null)
                throw new InvalidConfigValueException("blockResistances: Invalid block name: " + blockName);
            
            try {
                float resist = Float.parseFloat(valueStr);
                block.setResistance(resist);
                LogHelper.debug("Changed resistance of %s to %g", blockName, resist);
            } catch(NumberFormatException ex) {
                throw new InvalidConfigValueException("blockResistances: Invalid number format", ex);
            }
        }
    }
    
    public static void save() {
        if(conf.hasChanged())
            conf.save();
    }
    
    /** Reloads the config values upon change */
    public static class ChangeHandler {
        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.modID.equals(Reference.MODID)) {
                loadValues();
                createBlacklistSet();
                modifyResistances();
                save();
            }
        }
    }
}
