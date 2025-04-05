package ljfa.tntutils;

public interface TNTUtilsConfigAccess {
	//General options
	boolean addExplodeCommand();
	boolean disableExplosions();
	float sizeMultiplier();
	boolean preventChainExplosions();
	boolean disableTNT();

	//Block damage options
	float dropChanceMultiplier();
	boolean disableBlockDamage();
	boolean disableBlockTriggering();
	boolean spareBlockEntities();

	//Entity damage options
	boolean disableEntityDamage();
	boolean disablePlayerDamage();
	boolean disableItemDamage();
	boolean disableMobDamage();

	static final String GENERAL_COMMENT = "General options";
	static final String ADD_EXPLODE_COMMAND_COMMENT = "Adds the '/explode' command";
	static final boolean ADD_EXPLODE_COMMAND_DEFAULT = true;
	static final String DISABLE_EXPLOSIONS_COMMENT = "Disables all effects from explosions";
	static final boolean DISABLE_EXPLOSIONS_DEFAULT = false;
	static final String SIZE_MULTIPLIER_COMMENT = "Multiplies the size of all explosions by this value";
	static final float SIZE_MULTIPLIER_MIN = 0.0f;
	static final float SIZE_MULTIPLIER_MAX = 50.0f;
	static final float SIZE_MULTIPLIER_DEFAULT = 1.0f;
	static final String PREVENT_CHAIN_EXPLOSIONS_COMMENT = "Prevents explosions from triggering TNT (blocks and minecarts), thus preventing chain explosions";
	static final boolean PREVENT_CHAIN_EXPLOSIONS_DEFAULT = false;
	static final String DISABLE_TNT_COMMENT = "Disables TNT (block and minecart) explosions";
	static final boolean DISABLE_TNT_DEFAULT = false;

	static final String BLOCK_DAMAGE_COMMENT = "Block damage options";
	static final String DROP_CHANCE_MULTIPLIER_COMMENT = """
			Multiplies the drop chance of blocks destroyed by explosions by this value (up to a maximum of 100%).
			0 = drop nothing, 1 = vanilla behavior, 2 = twice as many drops, etc.

			Will not affect explosions without block drop decay (i.e. TNT explosions by default).
			To get a drop chance of 100%, set the game rules "mobExplosionDropDecay", "tntExplosionDropDecay" and
			"blockExplosionDropDecay" to "false".""";
	static final float DROP_CHANCE_MULTIPLIER_MIN = 0.0f;
	static final float DROP_CHANCE_MULTIPLIER_DEFAULT = 1.0f;
	static final String DISABLE_BLOCK_DAMAGE_COMMENT = "Prevents explosions from destroying blocks. Will not affect blocks tagged with #tntutils:explosion_whitelist.\n"
			+ "Individual blocks can be exempted from explosion damage by tagging them with #tntutils:explosion_blacklist.";
	static final boolean DISABLE_BLOCK_DAMAGE_DEFAULT = false;
	static final String DISABLE_BLOCK_TRIGGERING_COMMENT = "Prevents explosions from triggering blocks (e.g. Wind Charge explosions flipping levers, etc.). Will not\n"
			+ "affect blocks tagged with #tntutils:trigger_whitelist.\n"
			+ "Individual blocks can be exempted from triggering by tagging them with #tntutils:trigger_whitelist.";
	static final boolean DISABLE_BLOCK_TRIGGERING_DEFAULT = false;
	static final String SPARE_BLOCK_ENTITIES_COMMENT = "Prevents explosions from destroying blocks with block entities. Will not affect blocks tagged with\n"
			+ "#tntutils:explosion_whitelist.";
	static final boolean SPARE_BLOCK_ENTITIES_DEFAULT = false;

	static final String ENTITY_DAMAGE_COMMENT = "Entity damage options";
	static final String DISABLE_ENTITY_DAMAGE_COMMENT = "Disables explosion damage to all entities (also includes minecarts, paintings, etc.). Will not affect entities\n"
			+ "tagged with #tntutils:explosion_whitelist.\n"
			+ "Individual entities can be exempted from explosion damage by tagging them with #tntutils:explosion_blacklist.";
	static final boolean DISABLE_ENTITY_DAMAGE_DEFAULT = false;
	static final String DISABLE_PLAYER_DAMAGE_COMMENT = "Disables explosion damage to players";
	static final boolean DISABLE_PLAYER_DAMAGE_DEFAULT = false;
	static final String DISABLE_ITEM_DAMAGE_COMMENT = "Disables explosion damage to items laying on the ground. Will not affect items tagged with #tntutils:explosion_whitelist.\n"
			+ "Individual items can be exempted from explosion damage by tagging them with #tntutils:explosion_blacklist.";
	static final boolean DISABLE_ITEM_DAMAGE_DEFAULT = false;
	static final String DISABLE_MOB_DAMAGE_COMMENT = "Disables explosion damage to mobs and animals. Will not affect entities tagged with #tntutils:explosion_whitelist.";
	static final boolean DISABLE_MOB_DAMAGE_DEFAULT = false;
}
