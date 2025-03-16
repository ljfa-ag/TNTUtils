package ljfa.tntutils.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraft.world.level.Explosion;

@Mixin(Explosion.class)
public abstract class ExplosionFabricMixin {
	@Inject(method = "explode", at = @At("HEAD"), cancellable = true)
	private void tntutils$onExplode(CallbackInfo ci) {
		if(ExplosionHandler.shouldCancelExplosion())
			ci.cancel();
		else
			ExplosionHandler.onExplosionStart((Explosion) (Object) this);
	}

	@Inject(method = "finalizeExplosion", at = @At("HEAD"), cancellable = true)
	private void tntutils$onFinalizeExplosion(CallbackInfo ci) {
		//FIXME: As opposed to the NeoForge implementation, this also cancels the sound and particle effects.
		//Might want to make sure that the behaviors align on both platforms.
		if(ExplosionHandler.shouldCancelExplosion())
			ci.cancel();
	}
}
