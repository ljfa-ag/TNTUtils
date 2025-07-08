package ljfa.tntutils.forge;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.command.ExplodeCommand;
import ljfa.tntutils.handlers.ExplosionHandler;
import ljfa.tntutils.mixin.BlockBehaviourAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(TNTUtils.MOD_ID)
public class TNTUtilsForgeEntry {
    public TNTUtilsForgeEntry(FMLJavaModLoadingContext context) {
        var modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        context.registerConfig(ModConfig.Type.COMMON, ForgeTNTUtilsConfig.commonSpec);
        /*if(FMLLoader.getDist() == Dist.CLIENT)
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);*/
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(this::modifyExplosionResistances);

        var eventBus = MinecraftForge.EVENT_BUS;
        if(ForgeTNTUtilsConfig.COMMON.addExplodeCommand())
            eventBus.addListener((RegisterCommandsEvent e) -> ExplodeCommand.register(e.getDispatcher()));
        eventBus.addListener(this::onExplosionStart);
    }

    private void onExplosionStart(ExplosionEvent.Start e) {
        if(ExplosionHandler.shouldCancelExplosion())
            e.setCanceled(true);
    }

    private void modifyExplosionResistances() {
        TNTUtils.logger.debug("Modifying explosion resistances");
        for(var entry : ForgeTNTUtilsConfig.COMMON.modifyExplosionResistances.get().valueMap().entrySet()) {
            try {
                var key = entry.getKey();
                var blockHolder = ForgeRegistries.BLOCKS.getDelegate(ResourceLocation.parse(key))
                        .orElseThrow(() -> new RuntimeException("Unknown block ID: \"" + key + "\""));
                if(!(entry.getValue() instanceof Number value))
                    throw new RuntimeException("The explosion resistance for \"" + key + "\" must be a number");
                var floatValue = value.floatValue();
                if(!(floatValue >= 0.0f)) //implicit check for NaN
                    throw new RuntimeException("The explosion resistance for \"" + key + "\" must be at least 0");

                ((BlockBehaviourAccessor) blockHolder.get()).setExplosionResistance(floatValue);
            }
            catch(Exception e) {
                TNTUtils.logger.error("Error reading the modifyExplosionResistances config value: " + e.getMessage());
            }
        }
    }
}
