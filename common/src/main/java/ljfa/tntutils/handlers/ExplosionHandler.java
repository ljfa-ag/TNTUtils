package ljfa.tntutils.handlers;

import ljfa.tntutils.TNTUtils;
import net.minecraft.world.level.Explosion;

public class ExplosionHandler {
	/**
	 * Called as or just before {@link Explosion#explode()} is called.
	 */
	public static void onExplosionStart(Explosion expl) {
		TNTUtils.logger.debug("onExplosionStart called. sizeMultiplier = {}", TNTUtils.config().sizeMultiplier());
	}
}
