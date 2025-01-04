package ljfa.tntutils.neoforge;

import ljfa.tntutils.TNTUtilsConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;

public class NeoforgeTNTUtilsConfig {
	public static class Common implements TNTUtilsConfig {
		public final DoubleValue sizeMultiplier;

		public Common(ModConfigSpec.Builder builder) {
			sizeMultiplier = builder
					.comment("Multiplies the size of all explosions by this")
					.translation("tntutils.config.sizeMultiplier")
					.defineInRange("sizeMultiplier", 1.0, 0.0, 50.0);
		}

		@Override
		public float sizeMultiplier() {
			return sizeMultiplier.get().floatValue();
		}
	}

	static final ModConfigSpec commonSpec;
	public static final Common COMMON;
	static {
		var specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}
}
