package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Redirect(
            method = "explode",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean redirectHurtEntity(Entity entity, DamageSource source, float amount) {
        if(ExplosionHandler.shouldDamageEntity(entity))
            return entity.hurt(source, amount);
        else
            return false;
    }
}
