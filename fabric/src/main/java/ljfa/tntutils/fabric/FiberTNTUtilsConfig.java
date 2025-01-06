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
import ljfa.tntutils.TNTUtilsConfig;
import net.fabricmc.loader.api.FabricLoader;

public class FiberTNTUtilsConfig {
	public static class Common implements TNTUtilsConfig {
		public final PropertyMirror<Float> sizeMultiplier = PropertyMirror.create(ConfigTypes.FLOAT);

		public ConfigTree buildConfig() {
			return ConfigTree.builder()
					.fork("general")
					.withComment("General options")

					.beginValue("sizeMultiplier", ConfigTypes.FLOAT.withMinimum(0.0f).withMaximum(50.0f), 1.0f)
					.withComment("Multiplies the size of all explosions by this")
					.finishValue(sizeMultiplier::mirror)

					.finishBranch()
					.build();
		}

		@Override
		public float sizeMultiplier() {
			return sizeMultiplier.getValue();
		}
	}

	static final Common COMMON = new Common();

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
