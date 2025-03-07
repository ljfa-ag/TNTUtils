package ljfa.tntutils.handlers;

import java.util.Optional;

import ljfa.tntutils.TNTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
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
		var config = TNTUtils.config();
		switch(explosion.getBlockInteraction()) {
			case DESTROY, DESTROY_WITH_DECAY -> {
				if(config.disableBlockDamage()
						|| config.spareBlockEntities() && state.hasBlockEntity())
					return false;
			}
			case TRIGGER_BLOCK -> {
				if(config.disableBlockTriggering())
					return false;
			}
			default -> {}
		}
		return original.shouldBlockExplode(explosion, reader, pos, state, power);
	}

	@Override
	public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
		var config = TNTUtils.config();
		if(config.disableEntityDamage()
				|| (config.disablePlayerDamage() && entity instanceof Player)
				|| (config.disableMobDamage() && entity instanceof Mob)
				|| (config.disableItemDamage() && entity instanceof ItemEntity))
			return false;
		else
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
