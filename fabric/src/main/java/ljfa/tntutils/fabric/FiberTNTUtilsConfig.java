package ljfa.tntutils.fabric;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;
import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.TNTUtilsConfigAccess;
import net.fabricmc.loader.api.FabricLoader;

public class FiberTNTUtilsConfig {
	public static class Common implements TNTUtilsConfigAccess {
		public final PropertyMirror<Boolean> addExplodeCommand = PropertyMirror.create(ConfigTypes.BOOLEAN);
		public final PropertyMirror<Float> sizeMultiplier = PropertyMirror.create(ConfigTypes.FLOAT);

		public final PropertyMirror<Float> dropChanceMultiplier = PropertyMirror.create(ConfigTypes.FLOAT);
		public final PropertyMirror<Boolean> disableBlockDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
		public final PropertyMirror<Boolean> disableBlockTriggering = PropertyMirror.create(ConfigTypes.BOOLEAN);
		public final PropertyMirror<Boolean> spareBlockEntities = PropertyMirror.create(ConfigTypes.BOOLEAN);

		public final PropertyMirror<Boolean> disableEntityDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
		public final PropertyMirror<Boolean> disablePlayerDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
		public final PropertyMirror<Boolean> disableItemDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
		public final PropertyMirror<Boolean> disableMobDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);

		public ConfigTree buildConfig() {
			return ConfigTree.builder()
					.fork("general")
					.withComment(GENERAL_COMMENT)

					.beginValue("addExplodeCommand", ConfigTypes.BOOLEAN, ADD_EXPLODE_COMMAND_DEFAULT)
					.withComment(ADD_EXPLODE_COMMAND_COMMENT)
					.finishValue(addExplodeCommand::mirror)

					.beginValue("sizeMultiplier", ConfigTypes.FLOAT.withMinimum(SIZE_MULTIPLIER_MIN).withMaximum(SIZE_MULTIPLIER_MAX), SIZE_MULTIPLIER_DEFAULT)
					.withComment(SIZE_MULTIPLIER_COMMENT)
					.finishValue(sizeMultiplier::mirror)

					.finishBranch()
					.fork("blockDamage")
					.withComment(BLOCK_DAMAGE_COMMENT)

					.beginValue("dropChanceMultiplier", ConfigTypes.FLOAT.withMinimum(DROP_CHANCE_MULTIPLIER_MIN), DROP_CHANCE_MULTIPLIER_DEFAULT)
					.withComment(DROP_CHANCE_MULTIPLIER_COMMENT)
					.finishValue(dropChanceMultiplier::mirror)

					.beginValue("disableBlockDamage", ConfigTypes.BOOLEAN, DISABLE_BLOCK_DAMAGE_DEFAULT)
					.withComment(DISABLE_BLOCK_DAMAGE_COMMENT)
					.finishValue(disableBlockDamage::mirror)

					.beginValue("disableBlockTriggering", ConfigTypes.BOOLEAN, DISABLE_BLOCK_TRIGGERING_DEFAULT)
					.withComment(DISABLE_BLOCK_TRIGGERING_COMMENT)
					.finishValue(disableBlockTriggering::mirror)

					.beginValue("spareBlockEntities", ConfigTypes.BOOLEAN, SPARE_BLOCK_ENTITIES_DEFAULT)
					.withComment(SPARE_BLOCK_ENTITIES_COMMENT)
					.finishValue(spareBlockEntities::mirror)

					.finishBranch()
					.fork("entityDamage")
					.withComment(ENTITY_DAMAGE_COMMENT)

					.beginValue("disableEntityDamage", ConfigTypes.BOOLEAN, DISABLE_ENTITY_DAMAGE_DEFAULT)
					.withComment(DISABLE_ENTITY_DAMAGE_COMMENT)
					.finishValue(disableEntityDamage::mirror)

					.beginValue("disablePlayerDamage", ConfigTypes.BOOLEAN, DISABLE_PLAYER_DAMAGE_DEFAULT)
					.withComment(DISABLE_PLAYER_DAMAGE_COMMENT)
					.finishValue(disablePlayerDamage::mirror)

					.beginValue("disableItemDamage", ConfigTypes.BOOLEAN, DISABLE_ITEM_DAMAGE_DEFAULT)
					.withComment(DISABLE_ITEM_DAMAGE_COMMENT)
					.finishValue(disableItemDamage::mirror)

					.beginValue("disableMobDamage", ConfigTypes.BOOLEAN, DISABLE_MOB_DAMAGE_DEFAULT)
					.withComment(DISABLE_MOB_DAMAGE_COMMENT)
					.finishValue(disableMobDamage::mirror)

					.finishBranch()
					.build();
		}

		@Override
		public boolean addExplodeCommand() {
			return addExplodeCommand.getValue();
		}

		@Override
		public float sizeMultiplier() {
			return sizeMultiplier.getValue();
		}

		@Override
		public float dropChanceMultiplier() {
			return dropChanceMultiplier.getValue();
		}

		@Override
		public boolean disableBlockDamage() {
			return disableBlockDamage.getValue();
		}

		@Override
		public boolean disableBlockTriggering() {
			return disableBlockTriggering.getValue();
		}

		@Override
		public boolean spareBlockEntities() {
			return spareBlockEntities.getValue();
		}

		@Override
		public boolean disableEntityDamage() {
			return disableEntityDamage.getValue();
		}

		@Override
		public boolean disablePlayerDamage() {
			return disablePlayerDamage.getValue();
		}

		@Override
		public boolean disableItemDamage() {
			return disableItemDamage.getValue();
		}

		@Override
		public boolean disableMobDamage() {
			return disableMobDamage.getValue();
		}
	}

	public static final Common COMMON = new Common();

	public static void init() {
		var configTree = COMMON.buildConfig();

		var configFile = FabricLoader.getInstance().getConfigDir().resolve("tntutils.json5");
		var serializer = new JanksonValueSerializer(false);

		//try reading the config file
		try(var reader = new BufferedInputStream(Files.newInputStream(configFile))) {
			FiberSerialization.deserialize(configTree, reader, serializer);
		}
		catch (NoSuchFileException ignored) {}
		catch (IOException | ValueDeserializationException e) {
			TNTUtils.logger.error("Error reading config file", e);
		}

		//write the config file
		//TODO: Can we avoid writing the config from scratch if nothing has changed?
		try(var writer = new BufferedOutputStream(Files.newOutputStream(configFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
			FiberSerialization.serialize(configTree, writer, serializer);
		}
		catch (IOException e) {
			TNTUtils.logger.error("Error writing config file", e);
		}
	}
}
