package ljfa.tntutils.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ljfa.tntutils.Config;
import ljfa.tntutils.handlers.EntityJoinHandler;
import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
    }
    
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ExplosionHandler());
        if(Config.disableTNT || Config.disableTNTMinecart)
            MinecraftForge.EVENT_BUS.register(new EntityJoinHandler());
    }
    
    public void postInit(FMLPostInitializationEvent event) {
        
    }
}
