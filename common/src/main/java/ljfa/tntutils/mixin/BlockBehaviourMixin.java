package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    //FIXME: This needs to be implemented differently since in 1.20, the EXPLOSION_RADIUS loot context param is set directly in Explosion.finalizeExplosion
    /*@ModifyExpressionValue(
            method = "onExplosionHit",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Explosion;radius()F")
    )
    private static float tntutils$modifyExplosionRadiusForLoot(float radius) {
        //Modify the value of LootContextParams.EXPLOSION_RADIUS that is set in BlockBehaviour#onExplosionHit.
        //The drop chance is 1.0f/radius, see ExplosionCondition and ApplyExplosionDecay.
        return radius / TNTUtils.config().dropChanceMultiplier();
    }*/
}
