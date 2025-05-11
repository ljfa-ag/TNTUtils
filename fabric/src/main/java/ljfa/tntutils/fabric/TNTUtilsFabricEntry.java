package ljfa.tntutils.fabric;

import java.util.HashMap;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.command.ExplodeCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class TNTUtilsFabricEntry implements ModInitializer {
    @Override
    public void onInitialize() {
        FiberTNTUtilsConfig.init();

        handleModifyExplosionResistances();

        if(FiberTNTUtilsConfig.COMMON.addExplodeCommand())
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ExplodeCommand.register(dispatcher));
    }

    private void handleModifyExplosionResistances() {
        /*
         * Since Fabric deliberately doesn't have a concept of a stage where all mod loading is finished, we can't be
         * sure when all blocks are registered. Rather, we need to use the RegistryEntryAddedCallback.
         * The drawback is that we can't check at this point that the block IDs in the config are spelled correctly.
         */
        var stringMap = FiberTNTUtilsConfig.COMMON.modifyExplosionResistances.getValue();
        if(stringMap.isEmpty())
            return;

        //parse the Strings in the config into ResourceKeys
        var resKeyMap = new HashMap<ResourceKey<Block>, Float>(stringMap.size());
        for(var entry : stringMap.entrySet()) {
            try {
                var resKey = ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(entry.getKey()));
                resKeyMap.put(resKey, entry.getValue());
            }
            catch(Exception e) {
                TNTUtils.logger.error("Error reading the modifyExplosionResistances config value: " + e.getMessage());
            }
        }

        RegistryEntryAddedCallback.allEntries(BuiltInRegistries.BLOCK, holder -> {
            Float value = resKeyMap.get(holder.key());
            if(value != null) {
                holder.value().explosionResistance = value;
                TNTUtils.logger.debug("Changed explosion resistance for " + holder.key().location());
            }
        });
    }
}
