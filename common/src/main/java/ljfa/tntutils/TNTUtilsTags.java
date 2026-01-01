package ljfa.tntutils;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TNTUtilsTags {
    //Block tags
    /**
     * Blocks tagged with this will not be destroyed by explosions (unless whitelisted).
     */
    public static final TagKey<Block> BLOCK_EXPLOSION_BLACKLIST = create(Registries.BLOCK, "explosion_blacklist");
    /**
     * Blocks tagged with this will always be destroyed by explosions, even when the "disableBlockDamage" or "spareBlockEntities"
     * config options are true. The whitelist takes precedence over the blacklist.
     */
    public static final TagKey<Block> BLOCK_EXPLOSION_WHITELIST = create(Registries.BLOCK, "explosion_whitelist");
    /**
     * Blocks tagged with this will not be triggered by Wind Charge explosions (unless whitelisted).
     */
    public static final TagKey<Block> BLOCK_TRIGGER_BLACKLIST = create(Registries.BLOCK, "trigger_blacklist");
    /**
     * Blocks tagged with this will always be triggered by Wind Charge explosions, even when the "disableBlockTriggering" config option is true.
     * The whitelist takes precedence over the blacklist.
     */
    public static final TagKey<Block> BLOCK_TRIGGER_WHITELIST = create(Registries.BLOCK, "trigger_whitelist");

    //Entity type tags
    /**
     * Entity types tagged with this will not be damaged by explosions (unless whitelisted).
     */
    public static final TagKey<EntityType<?>> ENTITY_TYPE_EXPLOSION_BLACKLIST = create(Registries.ENTITY_TYPE, "explosion_blacklist");
    /**
     * Entity types tagged with this will always be damaged by explosions, even when one of the entity damage config options is turned on.
     * The whitelist takes precedence over the blacklist.
     */
    public static final TagKey<EntityType<?>> ENTITY_TYPE_EXPLOSION_WHITELIST = create(Registries.ENTITY_TYPE, "explosion_whitelist");
    /**
     * Entity types tagged with this will not be able to create explosions (unless whitelisted).
     * This will apply both to direct and indirect source entities.
     */
    public static final TagKey<EntityType<?>> ENTITY_TYPE_EXPLOSION_SOURCE_BLACKLIST = create(Registries.ENTITY_TYPE, "explosion_source_blacklist");
    /**
     * Entity types tagged with this will always be able to create explosions, even when the "disableExplosions" config option is true.
     * This will apply both to direct and indirect source entities. The whitelist takes precedence over the blacklist.
     */
    public static final TagKey<EntityType<?>> ENTITY_TYPE_EXPLOSION_SOURCE_WHITELIST = create(Registries.ENTITY_TYPE, "explosion_source_whitelist");

    //Item tags
    /**
     * Items tagged with this will not be damaged by explosions in ItemEntity form (unless whitelisted).
     */
    public static final TagKey<Item> ITEM_EXPLOSION_BLACKLIST = create(Registries.ITEM, "explosion_blacklist");
    /**
     * Items tagged with this will always be damaged by explosions, even when the "disableItemDamage" config option is true,
     * The whitelist takes precedence over the blacklist.
     */
    public static final TagKey<Item> ITEM_EXPLOSION_WHITELIST = create(Registries.ITEM, "explosion_whitelist");

    private static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registryKey, String name) {
        return TagKey.create(registryKey, Identifier.fromNamespaceAndPath(TNTUtils.MOD_ID, name));
    }
}
