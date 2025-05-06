package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ljfa.tntutils.TNTUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

@Mixin(MinecartTNT.class)
public abstract class MinecartTNTMixin extends AbstractMinecart {
	@Shadow(prefix = "shadow$") //use prefix to silence warning about not overriding the package-private method VehicleEntity.getDropItem()
	protected abstract Item shadow$getDropItem();

	//Disarm the TNT minecart as soon as it is ignited
	@Inject(method = "primeFuse",
			at = @At("HEAD"),
			cancellable = true)
	private void onPrimeFuse(CallbackInfo ci) {
		if(TNTUtils.config().disableTNT() && !this.level().isClientSide()) {
			this.destroy(this.shadow$getDropItem());
			ci.cancel();
		}
	}

	//Make sure the TNT minecart also doesn't explode when falling down or being shot by a flaming arrow
	@Inject(method = "explode(Lnet/minecraft/world/damagesource/DamageSource;D)V",
			at = @At("HEAD"),
			cancellable = true)
	private void onExplode(CallbackInfo ci) {
		if(TNTUtils.config().disableTNT() && !this.level().isClientSide()) {
			this.destroy(this.shadow$getDropItem());
			ci.cancel();
		}
	}

	private MinecartTNTMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
}
