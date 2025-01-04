package ljfa.tntutils.neoforge;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.handlers.ExplosionHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ExplosionEvent;

@Mod(TNTUtils.MOD_ID)
public class TNTUtilsNeoforgeEntry {
	public TNTUtilsNeoforgeEntry(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::commonSetup);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		var eventBus = NeoForge.EVENT_BUS;

		eventBus.addListener((ExplosionEvent.Start e) -> ExplosionHandler.onExplosionStart(e.getExplosion()));
	}
}
