package ljfa.tntutils.neoforge;

import ljfa.tntutils.TNTUtilsConfigAccess;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;

public class NeoforgeTNTUtilsConfig {
	public static class Common implements TNTUtilsConfigAccess {
		public final DoubleValue sizeMultiplier;
		public final BooleanValue addExplodeCommand;
		public final DoubleValue dropChanceMultiplier;

		public Common(ModConfigSpec.Builder builder) {
			builder.comment(GENERAL_COMMENT).push("general");
			sizeMultiplier = builder
					.comment(SIZE_MULTIPLIER_COMMENT)
					.translation(SIZE_MULTIPLIER_KEY)
					.defineInRange("sizeMultiplier", SIZE_MULTIPLIER_DEFAULT, SIZE_MULTIPLIER_MIN, SIZE_MULTIPLIER_MAX);
			addExplodeCommand = builder
					.comment(ADD_EXPLODE_COMMAND_COMMENT)
					.translation(ADD_EXPLODE_COMMAND_KEY)
					.gameRestart()
					.define("addExplodeCommand", ADD_EXPLODE_COMMAND_DEFAULT);

			builder.pop().comment(BLOCK_DAMAGE_COMMENT).push("blockDamage");
			dropChanceMultiplier = builder
					.comment(DROP_CHANCE_MULTIPLIER_COMMENT)
					.translation(DROP_CHANCE_MULTIPLIER_KEY)
					.defineInRange("dropChanceMultiplier", DROP_CHANCE_MULTIPLIER_DEFAULT, DROP_CHANCE_MULTIPLIER_MIN, Double.POSITIVE_INFINITY);
		}

		@Override
		public float sizeMultiplier() {
			return sizeMultiplier.get().floatValue();
		}

		@Override
		public boolean addExplodeCommand() {
			return addExplodeCommand.get();
		}

		@Override
		public float dropChanceMultiplier() {
			return dropChanceMultiplier.get().floatValue();
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
