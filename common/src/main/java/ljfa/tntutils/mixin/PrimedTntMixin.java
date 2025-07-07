package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.handlers.ExplosionHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;

@Mixin(PrimedTnt.class)
public abstract class PrimedTntMixin extends Entity {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        if(TNTUtils.config().disableTNT() && !this.level().isClientSide()) {
            ExplosionHandler.disarmPrimedTnt((PrimedTnt) (Object) this);
            ci.cancel();
        }
    }

    private PrimedTntMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
}
