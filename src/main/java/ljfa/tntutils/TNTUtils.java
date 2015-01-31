package ljfa.tntutils;

import ljfa.tntutils.command.CommandExplosion;
import ljfa.tntutils.proxy.CommonProxy;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;

@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.VERSION, acceptableRemoteVersions = "*")
public class TNTUtils {
    @Mod.Instance(Reference.MODID)
    public static TNTUtils instance;
    
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
    
    public static final boolean deobfuscatedEnv = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
    
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
