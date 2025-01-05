package ljfa.tntutils.fabric;

import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;
import ljfa.tntutils.TNTUtilsConfig;

public class FiberTNTUtilsConfig {
	public static class Common implements TNTUtilsConfig {
		public final PropertyMirror<Float> sizeMultiplier = PropertyMirror.create(ConfigTypes.FLOAT);

		public ConfigTree buildConfig() {
			return ConfigTree.builder()
					.beginValue("sizeMultiplier", ConfigTypes.FLOAT.withMinimum(0.0f).withMaximum(50.0f), 1.0f)
					.withComment("Multiplies the size of all explosions by this")
					.finishValue(sizeMultiplier::mirror)
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
	}
}
