package ljfa.tntutils.fabric;

import ljfa.tntutils.command.ExplodeCommand;
import ljfa.tntutils.command.ExplosionInteractionArgument;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;

public class TNTUtilsFabricEntry implements ModInitializer {
	@Override
	public void onInitialize() {
		FiberTNTUtilsConfig.init();

		ArgumentTypeRegistry.registerArgumentType(
				ExplosionInteractionArgument.ID,
				ExplosionInteractionArgument.class,
				SingletonArgumentInfo.contextFree(ExplosionInteractionArgument::new));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ExplodeCommand.register(dispatcher));
	}
}
