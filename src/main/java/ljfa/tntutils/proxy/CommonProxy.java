package ljfa.tntutils.proxy;

import ljfa.tntutils.Config;
import ljfa.tntutils.blocks.ModBlocks;
import ljfa.tntutils.util.LogHelper;
import ljfa.tntutils.util.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.RegistrySimple;

import org.apache.logging.log4j.Level;

import com.google.common.collect.BiMap;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
        ModBlocks.preInit();
        
        if(Config.replaceTNT)
            replaceVanillaTNT();
    }
    
    public void init(FMLInitializationEvent event) {
        
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
            ObjectIntIdentityMap intMap = (ObjectIntIdentityMap)ReflectionHelper.getField(RegistryNamespaced.class, "underlyingIntegerMap", Block.blockRegistry);
            intMap.func_148746_a(ModBlocks.replaced_tnt, tntID);
            
            //Replace it in the "registryObjects"
            BiMap regMap = (BiMap)ReflectionHelper.getField(RegistrySimple.class, "registryObjects", Block.blockRegistry);
            regMap.forcePut("minecraft:tnt", ModBlocks.replaced_tnt);
            
            //Replace it in the associated ItemBlock
            ItemBlock tntItem = (ItemBlock)Item.itemRegistry.getObjectById(tntID);
            ReflectionHelper.setFinalField(ItemBlock.class, "field_150939_a", tntItem, ModBlocks.replaced_tnt);
            
            //Replace it in the Blocks class
            ReflectionHelper.setFinalField(Blocks.class, "tnt", null, ModBlocks.replaced_tnt);
            LogHelper.info("Replaced Vanilla TNT");
        } catch(Exception ex) {
            LogHelper.log(Level.ERROR, ex, "Failed to replace Vanilla TNT!");
        }
    }
}
