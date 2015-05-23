package ljfa.tntutils.proxy;

import ljfa.tntutils.Config;
import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.blocks.BlockReplacedTNT;
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

import com.google.common.collect.BiMap;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
        
        if(Config.preventChainExpl) {
            TNTUtils.replaced_tnt = new BlockReplacedTNT().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("tnt").setBlockTextureName("tnt");
            replaceVanillaTNT();
        }
    }
    
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ExplosionHandler());
        if(Config.disableTNT || Config.disableTNTMinecart)
            MinecraftForge.EVENT_BUS.register(new EntityJoinHandler());
    }
    
    public void postInit(FMLPostInitializationEvent event) {
        
    }
    
    @SuppressWarnings("unchecked")
    private void replaceVanillaTNT() {
        try {
            LogHelper.info("About to replace Vanilla TNT");
            
            //Get TNT object to replace
            Block oldTNT = (Block)Block.blockRegistry.getObject("tnt");
            int tntID = Block.blockRegistry.getIDForObject(oldTNT);

            //Replace it in the "underlyingIntegerMap"
            String fieldName = Utils.deobfuscatedEnv ? "underlyingIntegerMap" : "field_148759_a";
            ObjectIntIdentityMap intMap = (ObjectIntIdentityMap)ReflectionHelper.getField(RegistryNamespaced.class, fieldName, Block.blockRegistry);
            intMap.func_148746_a(TNTUtils.replaced_tnt, tntID);
            
            //Replace it in the "registryObjects"
            fieldName = Utils.deobfuscatedEnv ? "registryObjects" : "field_82596_a";
            BiMap<String, Block> regMap = (BiMap)ReflectionHelper.getField(RegistrySimple.class, fieldName, Block.blockRegistry);
            regMap.forcePut("minecraft:tnt", TNTUtils.replaced_tnt);
            
            //Replace it in the associated ItemBlock
            ItemBlock tntItem = (ItemBlock)Item.itemRegistry.getObjectById(tntID);
            ReflectionHelper.setFinalField(ItemBlock.class, "field_150939_a", tntItem, TNTUtils.replaced_tnt);
            
            //Replace it in the Blocks class
            fieldName = Utils.deobfuscatedEnv ? "tnt" : "field_150335_W";
            ReflectionHelper.setFinalField(Blocks.class, fieldName, null, TNTUtils.replaced_tnt);
            LogHelper.info("Replaced Vanilla TNT");
        } catch(Exception ex) {
            throw new RuntimeException("Failed to replace Vanilla TNT!", ex);
        }
    }
}
