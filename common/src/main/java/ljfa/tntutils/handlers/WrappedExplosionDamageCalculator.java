package ljfa.tntutils.handlers;

import java.util.Optional;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.TNTUtilsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class WrappedExplosionDamageCalculator extends ExplosionDamageCalculator {
    private final ExplosionDamageCalculator original;

    //Cache config values to avoid reading them thousands of times per explosion
    private final boolean disableBlockDamage        = TNTUtils.config().disableBlockDamage();
    private final boolean disableCreeperBlockDamage = TNTUtils.config().disableCreeperBlockDamage();
    private final boolean spareBlockEntities        = TNTUtils.config().spareBlockEntities();

    public WrappedExplosionDamageCalculator(ExplosionDamageCalculator original) {
        this.original = original;
    }

    @Override
    public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, FluidState fluid) {
        return original.getBlockExplosionResistance(explosion, reader, pos, state, fluid);
    }

    @Override
    public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
        if(explosion.interactsWithBlocks()) {
            if(
                    (disableBlockDamage
                    || (disableCreeperBlockDamage && explosion.getDirectSourceEntity() instanceof Creeper)
                    || (spareBlockEntities && state.hasBlockEntity())
                    || state.is(TNTUtilsTags.BLOCK_EXPLOSION_BLACKLIST))
                && !state.is(TNTUtilsTags.BLOCK_EXPLOSION_WHITELIST)
            )
                return false;
        }
        return original.shouldBlockExplode(explosion, reader, pos, state, power);
    }

    //FIXME: We need to use mixins to implement these
    /*@Override
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        if(
                (disableEntityDamage
                || (disablePlayerDamage && entity instanceof Player)
                || (disableMobDamage && entity instanceof Mob)
                || (entity instanceof ItemEntity ie && shouldSpareItemEntity(ie))
                || entity.getType().is(TNTUtilsTags.ENTITY_EXPLOSION_BLACKLIST))
            && !entity.getType().is(TNTUtilsTags.ENTITY_EXPLOSION_WHITELIST)
        )
            return false;
        return original.shouldDamageEntity(explosion, entity);
    }

    private boolean shouldSpareItemEntity(ItemEntity entity) {
        return (disableItemDamage || entity.getItem().is(TNTUtilsTags.ITEM_EXPLOSION_BLACKLIST))
                && !entity.getItem().is(TNTUtilsTags.ITEM_EXPLOSION_WHITELIST);
    }

    @Override
    public float getKnockbackMultiplier(Entity entity) {
        return original.getKnockbackMultiplier(entity);
    }

    @Override
    public float getEntityDamageAmount(Explosion explosion, Entity entity) {
        return original.getEntityDamageAmount(explosion, entity);
    }*/
}
