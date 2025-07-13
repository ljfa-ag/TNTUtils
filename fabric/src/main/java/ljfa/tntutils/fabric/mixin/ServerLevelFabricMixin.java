package ljfa.tntutils.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraft.server.level.ServerLevel;

@Mixin(ServerLevel.class)
public abstract class ServerLevelFabricMixin {
    @Inject(method = "explode", at = @At("HEAD"), cancellable = true, require = 1)
    private void onExplode(CallbackInfo ci) {
        if(ExplosionHandler.shouldCancelExplosion())
            ci.cancel();
    }
}
