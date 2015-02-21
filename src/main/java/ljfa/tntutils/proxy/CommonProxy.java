package ljfa.tntutils.proxy;

import ljfa.tntutils.Config;
import ljfa.tntutils.blocks.ModBlocks;
import ljfa.tntutils.handlers.EntityJoinHandler;
import ljfa.tntutils.handlers.ExplosionHandler;
import ljfa.tntutils.util.LogHelper;
import ljfa.tntutils.util.ReflectionHelper;
import ljfa.tntutils.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.RegistrySimple;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.google.common.collect.BiMap;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
        ModBlocks.preInit();
        
        if(Config.preventChainExpl)
            replaceVanillaTNT();
    }
    
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ExplosionHandler());
        if(Config.disableTNT || Config.disableTNTMinecart)
            MinecraftForge.EVENT_BUS.register(new EntityJoinHandler());
    }
    
    public void postInit(FMLPostInitializationEvent event) {
        
    }
    
    private void replaceVanillaTNT() {
        try {
            LogHelper.info("About to replace Vanilla TNT");
            
            //Get TNT object to replace
            Block oldTNT = (Block)Block.blockRegistry.getObject("tnt");
            int tntID = Block.blockRegistry.getIDForObject(oldTNT);

            //Replace it in the "underlyingIntegerMap"
            String fieldName = Utils.deobfuscatedEnv ? "underlyingIntegerMap" : "field_148759_a";
            ObjectIntIdentityMap intMap = (ObjectIntIdentityMap)ReflectionHelper.getField(RegistryNamespaced.class, fieldName, Block.blockRegistry);
            intMap.put(ModBlocks.replaced_tnt, tntID);
            
            //Replace it in the "registryObjects"
            fieldName = Utils.deobfuscatedEnv ? "registryObjects" : "field_82596_a";
            BiMap regMap = (BiMap)ReflectionHelper.getField(RegistrySimple.class, fieldName, Block.blockRegistry);
            regMap.forcePut("minecraft:tnt", ModBlocks.replaced_tnt);
            
            //Replace it in the associated ItemBlock
            ItemBlock tntItem = (ItemBlock)Item.itemRegistry.getObjectById(tntID);
            fieldName = Utils.deobfuscatedEnv ? "block" : "field_150939_a";
            ReflectionHelper.setFinalField(ItemBlock.class, fieldName, tntItem, ModBlocks.replaced_tnt);
            
            //Replace it in the Blocks class
            fieldName = Utils.deobfuscatedEnv ? "tnt" : "field_150335_W";
            ReflectionHelper.setFinalField(Blocks.class, fieldName, null, ModBlocks.replaced_tnt);
            LogHelper.info("Replaced Vanilla TNT");
        } catch(Exception ex) {
            throw new RuntimeException("Failed to replace Vanilla TNT!", ex);
        }
    }
}
