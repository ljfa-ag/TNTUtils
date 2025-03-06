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
		public final BooleanValue disableBlockTriggering;
		public final BooleanValue spareBlockEntities;

		public Common(ModConfigSpec.Builder builder) {
			builder.comment(GENERAL_COMMENT).push("general");
			addExplodeCommand = builder
					.comment(ADD_EXPLODE_COMMAND_COMMENT)
					.gameRestart()
					.define("addExplodeCommand", ADD_EXPLODE_COMMAND_DEFAULT);
			sizeMultiplier = builder
					.comment(SIZE_MULTIPLIER_COMMENT)
					.defineInRange("sizeMultiplier", SIZE_MULTIPLIER_DEFAULT, SIZE_MULTIPLIER_MIN, SIZE_MULTIPLIER_MAX);

			builder.pop().comment(BLOCK_DAMAGE_COMMENT).push("blockDamage");
			dropChanceMultiplier = builder
					.comment(DROP_CHANCE_MULTIPLIER_COMMENT)
					.defineInRange("dropChanceMultiplier", DROP_CHANCE_MULTIPLIER_DEFAULT, DROP_CHANCE_MULTIPLIER_MIN, Double.POSITIVE_INFINITY);
			disableBlockDamage = builder
					.comment(DISABLE_BLOCK_DAMAGE_COMMENT)
					.define("disableBlockDamage", DISABLE_BLOCK_DAMAGE_DEFAULT);
			disableBlockTriggering = builder
					.comment(DISABLE_BLOCK_TRIGGERING_COMMENT)
					.define("disableBlockTriggering", DISABLE_BLOCK_TRIGGERING_DEFAULT);
			spareBlockEntities = builder
					.comment(SPARE_BLOCK_ENTITIES_COMMENT)
					.define("spareBlockEntities", SPARE_BLOCK_ENTITIES_DEFAULT);
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

		@Override
		public boolean disableBlockTriggering() {
			return disableBlockTriggering.get();
		}

		@Override
		public boolean spareBlockEntities() {
			return spareBlockEntities.get();
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
