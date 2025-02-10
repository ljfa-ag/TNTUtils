package ljfa.tntutils.neoforge;

import ljfa.tntutils.TNTUtilsConfigAccess;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;

public class NeoforgeTNTUtilsConfig {
	public static class Common implements TNTUtilsConfigAccess {
		public final BooleanValue addExplodeCommand;
		public final DoubleValue sizeMultiplier;

		public final DoubleValue dropChanceMultiplier;
		public final BooleanValue disableBlockDamage;

		public Common(ModConfigSpec.Builder builder) {
			builder.comment(GENERAL_COMMENT).push("general");
			addExplodeCommand = builder
					.comment(ADD_EXPLODE_COMMAND_COMMENT)
					.translation(ADD_EXPLODE_COMMAND_TRANSLATION)
					.gameRestart()
					.define("addExplodeCommand", ADD_EXPLODE_COMMAND_DEFAULT);
			sizeMultiplier = builder
					.comment(SIZE_MULTIPLIER_COMMENT)
					.translation(SIZE_MULTIPLIER_TRANSLATION)
					.defineInRange("sizeMultiplier", SIZE_MULTIPLIER_DEFAULT, SIZE_MULTIPLIER_MIN, SIZE_MULTIPLIER_MAX);

			builder.pop().comment(BLOCK_DAMAGE_COMMENT).push("blockDamage");
			dropChanceMultiplier = builder
					.comment(DROP_CHANCE_MULTIPLIER_COMMENT)
					.translation(DROP_CHANCE_MULTIPLIER_TRANSLATION)
					.defineInRange("dropChanceMultiplier", DROP_CHANCE_MULTIPLIER_DEFAULT, DROP_CHANCE_MULTIPLIER_MIN, Double.POSITIVE_INFINITY);
			disableBlockDamage = builder
					.comment(DISABLE_BLOCK_DAMAGE_COMMENT)
					.translation(DISABLE_BLOCK_DAMAGE_TRANSLATION)
					.define("disableBlockDamage", DISABLE_BLOCK_DAMAGE_DEFAULT);
		}

		@Override
		public boolean addExplodeCommand() {
			return addExplodeCommand.get();
		}

		@Override
		public float sizeMultiplier() {
			return sizeMultiplier.get().floatValue();
		}

		@Override
		public float dropChanceMultiplier() {
			return dropChanceMultiplier.get().floatValue();
		}

		@Override
		public boolean disableBlockDamage() {
			return disableBlockDamage.get();
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
