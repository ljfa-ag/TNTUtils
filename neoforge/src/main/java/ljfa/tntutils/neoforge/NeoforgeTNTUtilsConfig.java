package ljfa.tntutils.neoforge;

import ljfa.tntutils.TNTUtilsConfigAccess;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;

public class NeoforgeTNTUtilsConfig {
	public static class Common implements TNTUtilsConfigAccess {
		public final DoubleValue sizeMultiplier;

		public Common(ModConfigSpec.Builder builder) {
			builder.comment(GENERAL_COMMENT).push("general");
			sizeMultiplier = builder
					.comment(SIZE_MULTIPLIER_COMMENT)
					.translation(SIZE_MULTIPLIER_KEY)
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
