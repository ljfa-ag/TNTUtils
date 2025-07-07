package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraft.world.level.Explosion;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    //the bottom-most constructor
    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Explosion$BlockInteraction;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/Holder;)V",
            at = @At("RETURN"),
            require = 1)
    private void onConstruct(CallbackInfo ci) {
        ExplosionHandler.onExplosionStart((Explosion) (Object) this);
    }
}
