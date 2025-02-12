package ljfa.tntutils.handlers;

import ljfa.tntutils.TNTUtils;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;

public class ExplosionHandler {
	/**
	 * Called as or just before {@link Explosion#explode()} is called.
	 */
	public static void onExplosionStart(Explosion expl) {
		expl.damageCalculator = new WrappedExplosionDamageCalculator(expl.damageCalculator);

		if(TNTUtils.config().disableBlockDamage() &&
				(expl.getBlockInteraction() == BlockInteraction.DESTROY || expl.getBlockInteraction() == BlockInteraction.DESTROY_WITH_DECAY)) {
			expl.blockInteraction = BlockInteraction.KEEP;
		}

		expl.radius *= TNTUtils.config().sizeMultiplier();
	}
}
