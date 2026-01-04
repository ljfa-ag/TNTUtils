package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.handlers.ExplosionHandler;
import ljfa.tntutils.handlers.WrappedExplosionDamageCalculator;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Shadow
    @Mutable
    private ExplosionDamageCalculator damageCalculator;

    @Shadow
    @Mutable
    private float radius;

    //the bottom-most constructor
    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Explosion$BlockInteraction;)V",
            at = @At("RETURN"))
    private void onConstruct(CallbackInfo ci) {
        damageCalculator = new WrappedExplosionDamageCalculator(damageCalculator);
        radius *= TNTUtils.config().sizeMultiplier();
    }

    @Redirect(method = "explode",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean redirectHurtEntity(Entity entity, DamageSource source, float amount) {
        if(ExplosionHandler.shouldDamageEntity(entity))
            return entity.hurt(source, amount * TNTUtils.config().entityDamageMultiplier());
        else
            return false;
    }

    @Redirect(method = "finalizeExplosion",
            slice = @Slice( //only target the call with LootContextParams.EXPLOSION_RADIUS as parameter, by looking at the slice after this field access
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/world/level/storage/loot/parameters/LootContextParams;EXPLOSION_RADIUS:Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;",
                            opcode = 178 /* GETSTATIC */)),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;withParameter(Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;Ljava/lang/Object;)Lnet/minecraft/world/level/storage/loot/LootParams$Builder;",
                    ordinal = 0))
    private <T> LootParams.Builder redirectWithParameter(LootParams.Builder builder, LootContextParam<T> parameter, T value) {
        //sanity check
        if(parameter == LootContextParams.EXPLOSION_RADIUS) {
            var radius = (Float) value;
            return builder.withParameter(LootContextParams.EXPLOSION_RADIUS, radius / TNTUtils.config().dropChanceMultiplier());
        }
        else {
            TNTUtils.logger.error("Incorrect call to LootParams$Builder.withParameter was redirected: " + parameter);
            return builder.withParameter(parameter, value);
        }
    }
}
