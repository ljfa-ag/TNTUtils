package ljfa.tntutils.fabric;

import ljfa.tntutils.command.ExplodeCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class TNTUtilsFabricEntry implements ModInitializer {
	@Override
	public void onInitialize() {
		FiberTNTUtilsConfig.init();

		if(FiberTNTUtilsConfig.COMMON.addExplodeCommand())
			CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ExplodeCommand.register(dispatcher));
	}
}
