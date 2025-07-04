package ljfa.tntutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import ljfa.tntutils.command.CommandExplosion;
import ljfa.tntutils.handlers.EntityJoinHandler;
import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.MODID, name = Reference.MODNAME, acceptableRemoteVersions = "*",
    guiFactory = Reference.GUI_FACTORY_CLASS, dependencies = "required-after:tnt_utilities_core",
    acceptedMinecraftVersions = "[1.12.2]", updateJSON = Reference.UPDATE_JSON)
public class TNTUtils {
    @Mod.Instance(Reference.MODID)
    public static TNTUtils instance;

    public static final Logger logger = LogManager.getLogger("TNTUtils", StringFormatterMessageFactory.INSTANCE);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ExplosionHandler());
        if(Config.disableTNT || Config.disableTNTMinecart)
            MinecraftForge.EVENT_BUS.register(new EntityJoinHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Config.createBlacklistSet();
        Config.modifyResistances();
        Config.save();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if(Config.explosionCommand) {
        	event.registerServerCommand(new CommandExplosion());
        }
    }
}
