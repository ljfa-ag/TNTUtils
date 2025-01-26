package ljfa.tntutils;

public interface TNTUtilsConfigAccess {
	float sizeMultiplier();

	static final String GENERAL_COMMENT = "General options";
	static final String SIZE_MULTIPLIER_COMMENT = "Multiplies the size of all explosions by this";
	static final String SIZE_MULTIPLIER_KEY = "tntutils.config.sizeMultiplier";
}
