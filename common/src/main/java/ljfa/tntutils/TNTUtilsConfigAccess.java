package ljfa.tntutils;

public interface TNTUtilsConfigAccess {
	//General options
	float sizeMultiplier();
	boolean addExplodeCommand();

	//Block damage options
	float dropChanceMultiplier();

	static final String GENERAL_COMMENT = "General options";
	static final String SIZE_MULTIPLIER_COMMENT = "Multiplies the size of all explosions by this value";
	static final String SIZE_MULTIPLIER_KEY = "tntutils.config.sizeMultiplier";
	static final float SIZE_MULTIPLIER_MIN = 0.0f;
	static final float SIZE_MULTIPLIER_MAX = 50.0f;
	static final float SIZE_MULTIPLIER_DEFAULT = 1.0f;
	static final String ADD_EXPLODE_COMMAND_COMMENT = "Adds the '/explode' command";
	static final String ADD_EXPLODE_COMMAND_KEY = "tntutils.config.addExplodeCommand";
	static final boolean ADD_EXPLODE_COMMAND_DEFAULT = true;

	static final String BLOCK_DAMAGE_COMMENT = "Block damage options";
	static final String DROP_CHANCE_MULTIPLIER_COMMENT = """
			Multiplies the drop chance of blocks destroyed by explosions by this value (up to a maximum of 100%).
			0 = drop nothing, 1 = vanilla behavior, 2 = twice as many drops, etc.

			Will not affect explosions without block drop decay (i.e. TNT explosions by default).
			To get a drop chance of 100%, set the game rules "mobExplosionDropDecay", "tntExplosionDropDecay" and
			"blockExplosionDropDecay" to "false".""";
	static final String DROP_CHANCE_MULTIPLIER_KEY = "tntutils.config.dropChanceMultiplier";
	static final float DROP_CHANCE_MULTIPLIER_MIN = 0.0f;
	static final float DROP_CHANCE_MULTIPLIER_DEFAULT = 1.0f;
}
