package ljfa.tntutils;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TNTUtilsTags {
	/**
	 * Blocks tagged with this will not be destroyed by explosions.
	 */
	public static final TagKey<Block> BLOCK_EXPLOSION_BLACKLIST = create(Registries.BLOCK, "explosion_blacklist");
	/**
	 * Blocks tagged with this will not be triggered by explosions (e.g. from Wind Charges).
	 */
	public static final TagKey<Block> BLOCK_EXPLOSION_TRIGGER_BLACKLIST = create(Registries.BLOCK, "explosion_trigger_blacklist");

	private static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registryKey, String name) {
		return TagKey.create(registryKey, ResourceLocation.fromNamespaceAndPath(TNTUtils.MOD_ID, name));
	}
}
