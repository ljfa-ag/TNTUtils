package ljfa.tntutils.neoforge;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.command.ExplodeCommand;
import ljfa.tntutils.handlers.ExplosionHandler;
import ljfa.tntutils.mixin.BlockBehaviourAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
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
        if(FMLEnvironment.getDist() == Dist.CLIENT)
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(this::modifyExplosionResistances);

        var eventBus = NeoForge.EVENT_BUS;
        if(NeoforgeTNTUtilsConfig.COMMON.addExplodeCommand())
            eventBus.addListener((RegisterCommandsEvent e) -> ExplodeCommand.register(e.getDispatcher()));
        eventBus.addListener(this::onExplosionStart);
    }

    private void onExplosionStart(ExplosionEvent.Start e) {
        if(!ExplosionHandler.shouldAllowExplosion(e.getExplosion().getDirectSourceEntity()))
            e.setCanceled(true);
    }

    private void modifyExplosionResistances() {
        TNTUtils.logger.debug("Modifying explosion resistances");
        for(var entry : NeoforgeTNTUtilsConfig.COMMON.modifyExplosionResistances.get().entrySet()) {
            try {
                var key = entry.getKey();
                var block = BuiltInRegistries.BLOCK.getOptional(Identifier.parse(key))
                        .orElseThrow(() -> new RuntimeException("Unknown block ID: \"" + key + "\""));
                if(!(entry.getValue() instanceof Number value))
                    throw new RuntimeException("The explosion resistance for \"" + key + "\" must be a number");
                var floatValue = value.floatValue();
                if(!(floatValue >= 0.0f)) //implicit check for NaN
                    throw new RuntimeException("The explosion resistance for \"" + key + "\" must be at least 0");

                ((BlockBehaviourAccessor) block).setExplosionResistance(floatValue);
            }
            catch(Exception e) {
                TNTUtils.logger.error("Error reading the modifyExplosionResistances config value: " + e.getMessage());
            }
        }
    }
}
