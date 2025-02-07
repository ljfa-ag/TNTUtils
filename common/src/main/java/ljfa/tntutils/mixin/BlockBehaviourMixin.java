package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import ljfa.tntutils.TNTUtils;
import net.minecraft.world.level.block.state.BlockBehaviour;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
	@ModifyExpressionValue(
			method = "onExplosionHit",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Explosion;radius()F")
	)
	private static float tntutils$modifyExplosionRadiusForLoot(float radius) {
		//Modify the value of LootContextParams.EXPLOSION_RADIUS that is set in BlockBehaviour#onExplosionHit.
		//The drop chance is 1.0f/radius, see ExplosionCondition and ApplyExplosionDecay.
		return radius / TNTUtils.config().dropChanceMultiplier();
	}
}
