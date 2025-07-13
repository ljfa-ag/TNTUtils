package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.handlers.WrappedExplosionDamageCalculator;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ServerExplosion;

@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin {
    @Shadow
    @Mutable
    private ExplosionDamageCalculator damageCalculator;

    @Shadow
    @Mutable
    private float radius;

    @Inject(method = "<init>",
            at = @At("RETURN"),
            require = 1)
    private void onConstruct(CallbackInfo ci) {
        damageCalculator = new WrappedExplosionDamageCalculator(damageCalculator);
        radius *= TNTUtils.config().sizeMultiplier();
    }
}
