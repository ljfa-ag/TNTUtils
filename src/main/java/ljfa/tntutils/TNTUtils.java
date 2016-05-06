package ljfa.tntutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import ljfa.tntutils.command.CommandExplosion;
import ljfa.tntutils.proxy.CommonProxy;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.VERSION, acceptableRemoteVersions = "*",
    guiFactory = Reference.GUI_FACTORY_CLASS, dependencies = "required-after:tnt_utilities_core",
    acceptedMinecraftVersions = "[1.8.9,1.9)", updateJSON = Reference.UPDATE_JSON)
public class TNTUtils {
    @Mod.Instance(Reference.MODID)
    public static TNTUtils instance;
    
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
    
    public static final Logger logger = LogManager.getLogger("TNTUtils", StringFormatterMessageFactory.INSTANCE);
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
        proxy.preInit(event);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Config.createBlacklistSet();
        Config.modifyResistances();
        Config.save();
        proxy.postInit(event);
    }
    
    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        if(Config.explosionCommand) {
            MinecraftServer server = MinecraftServer.getServer();
            ServerCommandManager commandManager = (ServerCommandManager)server.getCommandManager();
            commandManager.registerCommand(new CommandExplosion());
        }
    }
}
