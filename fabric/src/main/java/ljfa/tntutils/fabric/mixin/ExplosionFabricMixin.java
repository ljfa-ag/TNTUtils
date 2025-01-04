package ljfa.tntutils.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraft.world.level.Explosion;

@Mixin(Explosion.class)
public abstract class ExplosionFabricMixin {
	@Inject(method = "explode", at = @At("HEAD"))
	private void tntutils$onExplode(CallbackInfo ci) {
		ExplosionHandler.onExplosionStart((Explosion) (Object) this);
	}
}
