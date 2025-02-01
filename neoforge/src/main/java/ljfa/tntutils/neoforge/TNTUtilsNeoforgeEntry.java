package ljfa.tntutils.neoforge;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.command.ExplodeCommand;
import ljfa.tntutils.handlers.ExplosionHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

@Mod(TNTUtils.MOD_ID)
public class TNTUtilsNeoforgeEntry {
	public TNTUtilsNeoforgeEntry(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::commonSetup);

		modContainer.registerConfig(ModConfig.Type.COMMON, NeoforgeTNTUtilsConfig.commonSpec);
		if(FMLLoader.getDist() == Dist.CLIENT)
			modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		var eventBus = NeoForge.EVENT_BUS;

		eventBus.addListener((RegisterCommandsEvent e) -> ExplodeCommand.register(e.getDispatcher()));

		eventBus.addListener((ExplosionEvent.Start e) -> ExplosionHandler.onExplosionStart(e.getExplosion()));
	}
}
