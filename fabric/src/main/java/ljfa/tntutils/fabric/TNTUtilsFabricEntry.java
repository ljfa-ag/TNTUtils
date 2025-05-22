package ljfa.tntutils.fabric;

import java.util.HashMap;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.command.ExplodeCommand;
import ljfa.tntutils.mixin.BlockBehaviourAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.registries.BuiltInRegistries;
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
        var stringMap = FiberTNTUtilsConfig.COMMON.modifyExplosionResistances.getValue();
        if(stringMap.isEmpty())
            return;

        //Not all mods' blocks might already be registered at this point, so we query the registry and store unknown IDs in this map.
        var unknownBlocks = new HashMap<ResourceLocation, Float>();
        for(var entry : stringMap.entrySet()) {
            try {
                var id = new ResourceLocation(entry.getKey());
                var block = BuiltInRegistries.BLOCK.get(id);
                if(block != null)
                    setExplosionResistance(id, block, entry.getValue());
                else
                    unknownBlocks.put(id, entry.getValue());
            }
            catch(Exception e) {
                TNTUtils.logger.error("Error reading the modifyExplosionResistances config value: " + e.getMessage());
            }
        }

        //install a callback for the unknown blocks
        if(!unknownBlocks.isEmpty()) {
            TNTUtils.logger.debug("Installing callback to change explosion resistance of " + unknownBlocks.size() + " blocks");
            RegistryEntryAddedCallback.event(BuiltInRegistries.BLOCK).register((rawId, id, block) -> {
                Float value = unknownBlocks.get(id);
                if(value != null)
                    setExplosionResistance(id, block, value);
            });
        }
    }

    private void setExplosionResistance(ResourceLocation id, Block block, float value) {
        ((BlockBehaviourAccessor) block).setExplosionResistance(value);
        TNTUtils.logger.debug("Changed explosion resistance for " + id);
    }
}
