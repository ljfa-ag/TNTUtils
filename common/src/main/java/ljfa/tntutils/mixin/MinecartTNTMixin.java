package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.level.Level;

@Mixin(MinecartTNT.class)
public abstract class MinecartTNTMixin extends AbstractMinecart {
    //Disarm the TNT minecart as soon as it is ignited
    @Inject(method = "primeFuse",
            at = @At("HEAD"),
            cancellable = true)
    private void onPrimeFuse(CallbackInfo ci) {
        if(!this.level().isClientSide() && !ExplosionHandler.shouldAllowTnt(null)) {
            super.destroy(damageSources().generic());
            ci.cancel();
        }
    }

    //Make sure the TNT minecart also doesn't explode when falling down or being shot by a flaming arrow
    @Inject(method = "explode(Lnet/minecraft/world/damagesource/DamageSource;D)V",
            at = @At("HEAD"),
            cancellable = true)
    private void onExplode(CallbackInfo ci) {
        if(!this.level().isClientSide() && !ExplosionHandler.shouldAllowTnt(null)) {
            super.destroy(damageSources().generic());
            ci.cancel();
        }
    }

    @Inject(method = "destroy",
            at = @At("HEAD"),
            cancellable = true)
    private void onDestroy(DamageSource source, CallbackInfo ci) {
        if(TNTUtils.config().preventChainExplosions() && source.is(DamageTypeTags.IS_EXPLOSION)) {
            super.destroy(source);
            ci.cancel();
        }
    }

    private MinecartTNTMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
}
