package ljfa.tntutils.neoforge;

import java.util.Map;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.InMemoryFormat;
import com.google.common.collect.ImmutableMap;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.TNTUtilsConfigAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;

public class NeoforgeTNTUtilsConfig {
	public static class Common implements TNTUtilsConfigAccess {
		public final BooleanValue addExplodeCommand;
		public final BooleanValue disableExplosions;
		public final DoubleValue sizeMultiplier;
		public final BooleanValue preventChainExplosions;
		public final BooleanValue disableTNT;

		public final DoubleValue dropChanceMultiplier;
		public final BooleanValue disableBlockDamage;
		public final BooleanValue disableBlockTriggering;
		public final BooleanValue spareBlockEntities;
		public final ConfigValue<Config> modifyExplosionResistances;
		private volatile Map<Block, Float> explosionResistanceMap; //volatile since ModConfigEvent.Reloading can be fired from any thread

		public final BooleanValue disableEntityDamage;
		public final BooleanValue disablePlayerDamage;
		public final BooleanValue disableItemDamage;
		public final BooleanValue disableMobDamage;

		public Common(ModConfigSpec.Builder builder) {
			//General
			builder.comment(GENERAL_COMMENT).push("general");
			addExplodeCommand = builder
					.comment(ADD_EXPLODE_COMMAND_COMMENT)
					.gameRestart()
					.define("addExplodeCommand", ADD_EXPLODE_COMMAND_DEFAULT);
			disableExplosions = builder
					.comment(DISABLE_EXPLOSIONS_COMMENT)
					.define("disableExplosions", DISABLE_EXPLOSIONS_DEFAULT);
			sizeMultiplier = builder
					.comment(SIZE_MULTIPLIER_COMMENT)
					.defineInRange("sizeMultiplier", SIZE_MULTIPLIER_DEFAULT, SIZE_MULTIPLIER_MIN, SIZE_MULTIPLIER_MAX);
			preventChainExplosions = builder
					.comment(PREVENT_CHAIN_EXPLOSIONS_COMMENT)
					.define("preventChainExplosions", PREVENT_CHAIN_EXPLOSIONS_DEFAULT);
			disableTNT = builder
					.comment(DISABLE_TNT_COMMENT)
					.define("disableTNT", DISABLE_TNT_DEFAULT);

			//Block damage
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
			modifyExplosionResistances = builder
					.comment(MODIFY_EXPLOSION_RESISTANCES_COMMENT + "\nThis is a table of entries of the form \"mod_id:block_id\" = value (the block ID must be double-quoted)")
					.define("modifyExplosionResistances", Config.wrap(Map.of(), InMemoryFormat.defaultInstance()), obj -> obj instanceof Config);

			//Entity damage
			builder.pop().comment(ENTITY_DAMAGE_COMMENT).push("entityDamage");
			disableEntityDamage = builder
					.comment(DISABLE_ENTITY_DAMAGE_COMMENT)
					.define("disableEntityDamage", DISABLE_ENTITY_DAMAGE_DEFAULT);
			disablePlayerDamage = builder
					.comment(DISABLE_PLAYER_DAMAGE_COMMENT)
					.define("disablePlayerDamage", DISABLE_PLAYER_DAMAGE_DEFAULT);
			disableItemDamage = builder
					.comment(DISABLE_ITEM_DAMAGE_COMMENT)
					.define("disableItemDamage", DISABLE_ITEM_DAMAGE_DEFAULT);
			disableMobDamage = builder
					.comment(DISABLE_MOB_DAMAGE_COMMENT)
					.define("disableMobDamage", DISABLE_MOB_DAMAGE_DEFAULT);
		}

		public void createExplosionResistanceMap() {
			TNTUtils.logger.debug("Creating explosion resistance map");
			var configTable = modifyExplosionResistances.get();
			var builder = ImmutableMap.<Block, Float>builderWithExpectedSize(configTable.size());
			for(var entry : configTable.entrySet()) {
				try {
					var key = entry.getKey();
					var block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse(key))
							.orElseThrow(() -> new RuntimeException("Unknown block ID: \"" + key + "\""));
					if(!(entry.getValue() instanceof Number value))
						throw new RuntimeException("The explosion resistance for \"" + key + "\" must be a number");
					var floatValue = value.floatValue();
					if(!(floatValue >= 0.0f)) //implicit check for NaN
						throw new RuntimeException("The explosion resistance for \"" + key + "\" must be at least 0");
					builder.put(block, floatValue);
				}
				catch(Exception e) {
					TNTUtils.logger.error("Error reading the modifyExplosionResistances config value: " + e.getMessage());
				}
			}
			explosionResistanceMap = builder.buildKeepingLast();
		}

		@Override
		public boolean addExplodeCommand() {
			return addExplodeCommand.get();
		}

		@Override
		public boolean disableExplosions() {
			return disableExplosions.get();
		}

		@Override
		public float sizeMultiplier() {
			return sizeMultiplier.get().floatValue();
		}

		@Override
		public boolean preventChainExplosions() {
			return preventChainExplosions.get();
		}

		@Override
		public boolean disableTNT() {
			return disableTNT.get();
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

		@Override
		public Map<Block, Float> explosionResistanceMap() {
			return explosionResistanceMap;
		}

		@Override
		public boolean disableEntityDamage() {
			return disableEntityDamage.get();
		}

		@Override
		public boolean disablePlayerDamage() {
			return disablePlayerDamage.get();
		}

		@Override
		public boolean disableItemDamage() {
			return disableItemDamage.get();
		}

		@Override
		public boolean disableMobDamage() {
			return disableMobDamage.get();
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
