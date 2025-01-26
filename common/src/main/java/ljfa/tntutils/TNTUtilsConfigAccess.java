package ljfa.tntutils;

public interface TNTUtilsConfigAccess {
	float sizeMultiplier();

	static final String GENERAL_COMMENT = "General options";
	static final String SIZE_MULTIPLIER_COMMENT = "Multiplies the size of all explosions by this";
	static final String SIZE_MULTIPLIER_KEY = "tntutils.config.sizeMultiplier";
	static final float SIZE_MULTIPLIER_MIN = 0.0f;
	static final float SIZE_MULTIPLIER_MAX = 50.0f;
	static final float SIZE_MULTIPLIER_DEFAULT = 1.0f;
}
