package ljfa.tntutils;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TNTUtilsTags {
	/**
	 * Blocks tagged with this will not be destroyed by explosions (unless whitelisted).
	 */
	public static final TagKey<Block> BLOCK_EXPLOSION_BLACKLIST = create(Registries.BLOCK, "explosion_blacklist");
	/**
	 * Blocks tagged with this will always be destroyed by explosions, even when the "disableBlockDamage" or "spareBlockEntities"
	 * options are true. The whitelist takes precedence over the blacklist.
	 */
	public static final TagKey<Block> BLOCK_EXPLOSION_WHITELIST = create(Registries.BLOCK, "explosion_whitelist");
	/**
	 * Blocks tagged with this will not be triggered by Wind Charge explosions (unless whitelisted).
	 */
	public static final TagKey<Block> BLOCK_TRIGGER_BLACKLIST = create(Registries.BLOCK, "trigger_blacklist");
	/**
	 * Blocks tagged with this will always be triggered by Wind Charge explosions, even when the "disableBlockTriggering" option is true.
	 * The whitelist takes precedence over the blacklist.
	 */
	public static final TagKey<Block> BLOCK_TRIGGER_WHITELIST = create(Registries.BLOCK, "trigger_whitelist");

	private static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registryKey, String name) {
		return TagKey.create(registryKey, ResourceLocation.fromNamespaceAndPath(TNTUtils.MOD_ID, name));
	}
}
