package ljfa.tntutils.handlers;

import java.util.Map;
import java.util.Optional;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.TNTUtilsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class WrappedExplosionDamageCalculator extends ExplosionDamageCalculator {
	private final ExplosionDamageCalculator original;

	//Cache config values to avoid reading them thousands of times per explosion
	private final boolean preventChainExplosions = TNTUtils.config().preventChainExplosions();
	private final boolean disableBlockDamage     = TNTUtils.config().disableBlockDamage();
	private final boolean spareBlockEntities     = TNTUtils.config().spareBlockEntities();
	private final boolean disableBlockTriggering = TNTUtils.config().disableBlockTriggering();
	private final boolean disableEntityDamage    = TNTUtils.config().disableEntityDamage();
	private final boolean disablePlayerDamage    = TNTUtils.config().disablePlayerDamage();
	private final boolean disableMobDamage       = TNTUtils.config().disableMobDamage();
	private final boolean disableItemDamage      = TNTUtils.config().disableItemDamage();
	private final Map<Block, Float> explosionResistanceMap = TNTUtils.config().explosionResistanceMap();

	public WrappedExplosionDamageCalculator(ExplosionDamageCalculator original) {
		this.original = original;
	}

	@Override
	public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, FluidState fluid) {
		return Optional.ofNullable(explosionResistanceMap.get(state.getBlock()))
				.or(() -> original.getBlockExplosionResistance(explosion, reader, pos, state, fluid));
	}

	@Override
	public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
		switch(explosion.getBlockInteraction()) {
			case DESTROY, DESTROY_WITH_DECAY -> {
				if(
						(disableBlockDamage
						|| (spareBlockEntities && state.hasBlockEntity())
						|| state.is(TNTUtilsTags.BLOCK_EXPLOSION_BLACKLIST))
					&& !state.is(TNTUtilsTags.BLOCK_EXPLOSION_WHITELIST)
				)
					return false;
			}
			case TRIGGER_BLOCK -> {
				if(
						(disableBlockTriggering
						|| state.is(TNTUtilsTags.BLOCK_TRIGGER_BLACKLIST))
					&& !state.is(TNTUtilsTags.BLOCK_TRIGGER_WHITELIST)
				)
					return false;
			}
			default -> {}
		}
		return original.shouldBlockExplode(explosion, reader, pos, state, power);
	}

	@Override
	public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
		if(
				(disableEntityDamage
				|| (disablePlayerDamage && entity instanceof Player)
				|| (disableMobDamage && entity instanceof Mob)
				|| (preventChainExplosions && entity instanceof MinecartTNT)
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
	}
}
