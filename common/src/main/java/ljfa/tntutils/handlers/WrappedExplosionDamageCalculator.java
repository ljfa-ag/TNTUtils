package ljfa.tntutils.handlers;

import java.util.Optional;

import ljfa.tntutils.TNTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class WrappedExplosionDamageCalculator extends ExplosionDamageCalculator {
	private final ExplosionDamageCalculator original;

	public WrappedExplosionDamageCalculator(ExplosionDamageCalculator original) {
		this.original = original;
	}

	@Override
	public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, FluidState fluid) {
		return original.getBlockExplosionResistance(explosion, reader, pos, state, fluid);
	}

	@Override
	public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
		if(TNTUtils.config().spareBlockEntities() && state.hasBlockEntity())
			return false;
		else
			return original.shouldBlockExplode(explosion, reader, pos, state, power);
	}

	@Override
	public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
		return original.shouldDamageEntity(explosion, entity);
	}

	@Override
	public float getKnockbackMultiplier(Entity entity) {
		return original.getKnockbackMultiplier(entity);
	}

	@Override
	public float getEntityDamageAmount(Explosion explosion, Entity entity) {
		return original.getEntityDamageAmount(explosion, entity);
	}
}
