package ljfa.tntutils.fabric;

import ljfa.tntutils.ExplodeCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class TNTUtilsFabricEntry implements ModInitializer {
	@Override
	public void onInitialize() {
		FiberTNTUtilsConfig.init();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ExplodeCommand.register(dispatcher));
	}
}
