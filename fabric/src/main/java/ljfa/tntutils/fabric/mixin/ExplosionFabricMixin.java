package ljfa.tntutils.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

@Mixin(Explosion.class)
public abstract class ExplosionFabricMixin {
    @Shadow
    private Level level;

    @Inject(method = "explode", at = @At("HEAD"), cancellable = true, require = 1)
    private void tntutils$onExplode(CallbackInfo ci) {
        if(ExplosionHandler.shouldCancelExplosion())
            ci.cancel();
        else
            ExplosionHandler.onExplosionStart((Explosion) (Object) this);
    }

    @Inject(method = "finalizeExplosion", at = @At("HEAD"), cancellable = true)
    private void tntutils$onFinalizeExplosion(CallbackInfo ci) {
        //Don't cancel finalizeExplosion on the client, so that particles and sound are still played, like in NeoForge
        if(!level.isClientSide() && ExplosionHandler.shouldCancelExplosion())
            ci.cancel();
    }
}
