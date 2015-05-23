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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.google.common.collect.BiMap;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
        
        if(Config.preventChainExpl) {
            TNTUtils.replaced_tnt = new BlockReplacedTNT().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("tnt");
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
            Block.blockRegistry.underlyingIntegerMap.put(TNTUtils.replaced_tnt, tntID);
            
            //Replace it in the "registryObjects"
            BiMap<ResourceLocation, Block> regMap = (BiMap)Block.blockRegistry.registryObjects;
            regMap.forcePut(new ResourceLocation("minecraft:tnt"), TNTUtils.replaced_tnt);
            
            //Replace it in the associated ItemBlock
            ItemBlock tntItem = (ItemBlock)Item.itemRegistry.getObjectById(tntID);
            tntItem.block = TNTUtils.replaced_tnt;
            
            //Replace it in the Blocks class
            Blocks.tnt = TNTUtils.replaced_tnt;
            
            LogHelper.info("Replaced Vanilla TNT");
        } catch(Exception ex) {
            throw new RuntimeException("Failed to replace Vanilla TNT!", ex);
        }
    }
}
