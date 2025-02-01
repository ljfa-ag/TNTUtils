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
		public final PropertyMirror<Float> sizeMultiplier = PropertyMirror.create(ConfigTypes.FLOAT);
		public final PropertyMirror<Boolean> addExplodeCommand = PropertyMirror.create(ConfigTypes.BOOLEAN);

		public ConfigTree buildConfig() {
			return ConfigTree.builder()
					.fork("general")
					.withComment(GENERAL_COMMENT)

					.beginValue("sizeMultiplier", ConfigTypes.FLOAT.withMinimum(SIZE_MULTIPLIER_MIN).withMaximum(SIZE_MULTIPLIER_MAX), SIZE_MULTIPLIER_DEFAULT)
					.withComment(SIZE_MULTIPLIER_COMMENT)
					.finishValue(sizeMultiplier::mirror)

					.beginValue("addExplodeCommand", ConfigTypes.BOOLEAN, ADD_EXPLODE_COMMAND_DEFAULT)
					.withComment(ADD_EXPLODE_COMMAND_COMMENT)
					.finishValue(addExplodeCommand::mirror)

					.finishBranch()
					.build();
		}

		@Override
		public float sizeMultiplier() {
			return sizeMultiplier.getValue();
		}

		@Override
		public boolean addExplodeCommand() {
			return addExplodeCommand.getValue();
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
