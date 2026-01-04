package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.handlers.WrappedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow
    @Mutable
    private ExplosionDamageCalculator damageCalculator;

    @Shadow
    @Mutable
    private float radius;

    //the bottom-most constructor
    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Explosion$BlockInteraction;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/Holder;)V",
            at = @At("RETURN"))
    private void onConstruct(CallbackInfo ci) {
        damageCalculator = new WrappedExplosionDamageCalculator(damageCalculator);
        radius *= TNTUtils.config().sizeMultiplier();
    }
}
