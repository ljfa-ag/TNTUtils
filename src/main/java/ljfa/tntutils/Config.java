package ljfa.tntutils;

import static ljfa.tntutils.TNTUtils.logger;

import java.io.File;
import java.util.IdentityHashMap;
import java.util.Map;

import ljfa.tntutils.exception.InvalidConfigValueException;
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
    public static Map<Block, Integer> blackWhiteList; //A map from block to a bitmask, where the set bits indicate the metadatas
    public static boolean listIsWhitelist;
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
        dropChanceModifier = (float)conf.get(CAT_GENERAL, "dropChanceIncrease", 0.0, "Modifies the chance that explosions will drop destroyed blocks as items\n"
                + "-1 = never drop anything, 0 = Vanilla behavior, 1 = always drop items.\n"
                + "Increasing will only affect explosions of size <= 10 since a large number of dropped items can cause lag.", -1.0, 1.0).getDouble();
        disableExplosions = conf.get(CAT_GENERAL, "disableExplosions", false, "Entirely disables all effects from explosions").getBoolean();
        preventChainExpl = conf.get(CAT_GENERAL, "preventChainExplosions", false, "Prevents explosions from triggering TNT and thus disables chain explosions").setRequiresMcRestart(true).getBoolean();
        disableTNT = conf.get(CAT_GENERAL, "disableTNT", false, "Disables TNT explosions").setRequiresMcRestart(true).getBoolean();
        disableTNTMinecart = conf.get(CAT_GENERAL, "disableTNTMinecart", false, "Disables the placement of TNT minecarts").setRequiresMcRestart(true).getBoolean();
        //----------------
        //destructionBlacklist is being read in createBlacklistSet()
        listIsWhitelist = conf.get(CAT_BLOCKDMG, "destructionListIsWhitelist", false, "If false, the list above is a blacklist. If true, it is a whitelist").getBoolean();
        disableBlockDamage = conf.get(CAT_BLOCKDMG, "disableBlockDamage", false, "Disables all block damage from explosions").getBoolean();
        disableCreeperBlockDamage = conf.get(CAT_BLOCKDMG, "disableCreeperBlockDamage", false, "\"Environmentally Friendly Creepers\": Makes creepers not destroy blocks").getBoolean();
        spareTileEntities = conf.get(CAT_BLOCKDMG, "disableTileEntityDamage", false, "Makes explosions not destroy blocks with tile entities").getBoolean();
        //----------------
        disableEntityDamage = conf.get(CAT_ENTDMG, "disableEntityDamage", false, "Disables explosion damage to all entities (also includes items, minecarts etc.)").getBoolean();
        disablePlayerDamage = conf.get(CAT_ENTDMG, "disablePlayerDamage", false, "Disables explosion damage to players").getBoolean();
        disableNPCDamage = conf.get(CAT_ENTDMG, "disableNPCDamage", false, "Disables explosion damage to animals and mobs").getBoolean();
        //----------------
    }
    
    public static void createBlacklistSet() {
        String[] blacklistArray = conf.get(CAT_BLOCKDMG, "destructionBlackOrWhitelist", new String[0], "A list of blocks (optionally with metadata) that will either never or only be destroyed by explosions\n"
                + "Whether this list is a blacklist or whitelist gets determined by the \"destructionListIsWhitelist\" option below\n"
                + "Syntax: modid:block or modid:block/meta").getStringList();
        
        blackWhiteList = new IdentityHashMap<Block, Integer>();
        for(String str: blacklistArray) {
            String blockname;
            int metamask;
            int ind = str.indexOf('/');
            if(ind != -1) {
                // blockname/meta
                String metaStr = str.substring(ind+1);
                int meta;
                try {
                    meta = Integer.parseInt(metaStr);
                } catch(NumberFormatException ex) {
                    throw new InvalidConfigValueException("destructionBlackOrWhitelist: Invalid number format: " + metaStr, ex);
                }
                if(meta < 0 || meta >= 16)
                    throw new InvalidConfigValueException("destructionBlackOrWhitelist: Metadata out of range: " + metaStr);
                
                blockname = str.substring(0, ind);
                metamask = 1 << meta;
            }
            else {
                // blockname without meta
                blockname = str;
                metamask = 0xFFFF;
            }
            
            Block block = (Block)Block.blockRegistry.getObject(blockname);
            if(block == Blocks.air || block == null)
                throw new InvalidConfigValueException("destructionBlackOrWhitelist: Invalid block name: " + blockname);
            
            if(!blackWhiteList.containsKey(block))
                blackWhiteList.put(block, metamask);
            else
                blackWhiteList.put(block, metamask | blackWhiteList.get(block));

            logger.debug("Added block to blacklist: %s, mask 0x%X", blockname, metamask);
        }
        blacklistActive = blackWhiteList.size() != 0;
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
                logger.debug("Changed resistance of %s to %g", blockName, resist);
            } catch(NumberFormatException ex) {
                throw new InvalidConfigValueException("blockResistances: Invalid number format: " + valueStr, ex);
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
                try {
                    loadValues();
                    createBlacklistSet();
                    modifyResistances();
                }
                finally {
                    save();
                }
            }
        }
    }
}
