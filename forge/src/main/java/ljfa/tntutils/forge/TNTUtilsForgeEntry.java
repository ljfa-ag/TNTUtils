package ljfa.tntutils.forge;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.command.ExplodeCommand;
import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(TNTUtils.MOD_ID)
public class TNTUtilsForgeEntry {
    public TNTUtilsForgeEntry(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, ForgeTNTUtilsConfig.commonSpec);
        if(FMLLoader.getDist() == Dist.CLIENT)
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(this::modifyExplosionResistances);

        var eventBus = NeoForge.EVENT_BUS;
        if(ForgeTNTUtilsConfig.COMMON.addExplodeCommand())
            eventBus.addListener((RegisterCommandsEvent e) -> ExplodeCommand.register(e.getDispatcher()));
        eventBus.addListener(this::onExplosionStart);
    }

    private void onExplosionStart(ExplosionEvent.Start e) {
        if(ExplosionHandler.shouldCancelExplosion())
            e.setCanceled(true);
        else
            ExplosionHandler.onExplosionStart(e.getExplosion());
    }

    private void modifyExplosionResistances() {
        TNTUtils.logger.debug("Modifying explosion resistances");
        for(var entry : ForgeTNTUtilsConfig.COMMON.modifyExplosionResistances.get().valueMap().entrySet()) {
            try {
                var key = entry.getKey();
                var block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse(key))
                        .orElseThrow(() -> new RuntimeException("Unknown block ID: \"" + key + "\""));
                if(!(entry.getValue() instanceof Number value))
                    throw new RuntimeException("The explosion resistance for \"" + key + "\" must be a number");
                var floatValue = value.floatValue();
                if(!(floatValue >= 0.0f)) //implicit check for NaN
                    throw new RuntimeException("The explosion resistance for \"" + key + "\" must be at least 0");

                block.explosionResistance = floatValue;
            }
            catch(Exception e) {
                TNTUtils.logger.error("Error reading the modifyExplosionResistances config value: " + e.getMessage());
            }
        }
    }
}
