package ljfa.tntutils.forge;

import java.util.Map;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.InMemoryFormat;

import ljfa.tntutils.TNTUtilsConfigAccess;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class ForgeTNTUtilsConfig {
    public static class Common implements TNTUtilsConfigAccess {
        public final BooleanValue addExplodeCommand;
        public final BooleanValue disableExplosions;
        public final DoubleValue sizeMultiplier;
        public final BooleanValue preventChainExplosions;
        public final BooleanValue disableTNT;

        public final DoubleValue dropChanceMultiplier;
        public final BooleanValue disableBlockDamage;
        public final BooleanValue disableCreeperBlockDamage;
        public final BooleanValue spareBlockEntities;
        public final ConfigValue<Config> modifyExplosionResistances;

        public final BooleanValue disableEntityDamage;
        public final BooleanValue disablePlayerDamage;
        public final BooleanValue disableItemDamage;
        public final BooleanValue disableMobDamage;
        public final DoubleValue entityDamageMultiplier;

        public Common(ForgeConfigSpec.Builder builder) {
            //General
            builder.comment(GENERAL_COMMENT).push("general");
            addExplodeCommand = builder
                    .comment(ADD_EXPLODE_COMMAND_COMMENT)
                    .define("addExplodeCommand", ADD_EXPLODE_COMMAND_DEFAULT);
            disableExplosions = builder
                    .comment(DISABLE_EXPLOSIONS_COMMENT)
                    .define("disableExplosions", DISABLE_EXPLOSIONS_DEFAULT);
            sizeMultiplier = builder
                    .comment(SIZE_MULTIPLIER_COMMENT)
                    .defineInRange("sizeMultiplier", SIZE_MULTIPLIER_DEFAULT, SIZE_MULTIPLIER_MIN, SIZE_MULTIPLIER_MAX);
            preventChainExplosions = builder
                    .comment(PREVENT_CHAIN_EXPLOSIONS_COMMENT)
                    .define("preventChainExplosions", PREVENT_CHAIN_EXPLOSIONS_DEFAULT);
            disableTNT = builder
                    .comment(DISABLE_TNT_COMMENT)
                    .define("disableTNT", DISABLE_TNT_DEFAULT);

            //Block damage
            builder.pop().comment(BLOCK_DAMAGE_COMMENT).push("blockDamage");
            dropChanceMultiplier = builder
                    .comment(DROP_CHANCE_MULTIPLIER_COMMENT)
                    .defineInRange("dropChanceMultiplier", DROP_CHANCE_MULTIPLIER_DEFAULT, DROP_CHANCE_MULTIPLIER_MIN, Double.POSITIVE_INFINITY);
            disableBlockDamage = builder
                    .comment(DISABLE_BLOCK_DAMAGE_COMMENT)
                    .define("disableBlockDamage", DISABLE_BLOCK_DAMAGE_DEFAULT);
            disableCreeperBlockDamage = builder
                    .comment(DISABLE_CREEPER_BLOCK_DAMAGE_COMMENT)
                    .define("disableCreeperBlockDamage", DISABLE_CREEPER_BLOCK_DAMAGE_DEFAULT);
            spareBlockEntities = builder
                    .comment(SPARE_BLOCK_ENTITIES_COMMENT)
                    .define("spareBlockEntities", SPARE_BLOCK_ENTITIES_DEFAULT);
            modifyExplosionResistances = builder
                    .comment(MODIFY_EXPLOSION_RESISTANCES_COMMENT)
                    .comment("""
                            Syntax:
                            EITHER as inline table (no newlines allowed):
                             modifyExplosionResistances = {"mod_id:block_id" = value, ...}
                            OR as subtable:
                             [blockDamage.modifyExplosionResistances]
                                "mod_id:block_id" = value
                                ...
                            Note that the block IDs must be double-quoted.""")
                            /* It might be confusing to users that by default, empty tables are serialized as {},
                             * but when values are added and the TOML file is written to, the inline table will be
                             * replaced by an ordinary subtable. Hence the syntax explanation for both.
                             */
                    .define("modifyExplosionResistances", Config.wrap(Map.of(), InMemoryFormat.defaultInstance()), obj -> obj instanceof Config);

            //Entity damage
            builder.pop().comment(ENTITY_DAMAGE_COMMENT).push("entityDamage");
            disableEntityDamage = builder
                    .comment(DISABLE_ENTITY_DAMAGE_COMMENT)
                    .define("disableEntityDamage", DISABLE_ENTITY_DAMAGE_DEFAULT);
            disablePlayerDamage = builder
                    .comment(DISABLE_PLAYER_DAMAGE_COMMENT)
                    .define("disablePlayerDamage", DISABLE_PLAYER_DAMAGE_DEFAULT);
            disableItemDamage = builder
                    .comment(DISABLE_ITEM_DAMAGE_COMMENT)
                    .define("disableItemDamage", DISABLE_ITEM_DAMAGE_DEFAULT);
            disableMobDamage = builder
                    .comment(DISABLE_MOB_DAMAGE_COMMENT)
                    .define("disableMobDamage", DISABLE_MOB_DAMAGE_DEFAULT);
            entityDamageMultiplier = builder
                    .comment(ENTITY_DAMAGE_MULTIPLIER_COMMENT)
                    .defineInRange("entityDamageMultiplier", ENTITY_DAMAGE_MULTIPLIER_DEFAULT, ENTITY_DAMAGE_MULTIPLIER_MIN, Double.POSITIVE_INFINITY);
        }

        @Override
        public boolean addExplodeCommand() {
            return addExplodeCommand.get();
        }

        @Override
        public boolean disableExplosions() {
            return disableExplosions.get();
        }

        @Override
        public float sizeMultiplier() {
            return sizeMultiplier.get().floatValue();
        }

        @Override
        public boolean preventChainExplosions() {
            return preventChainExplosions.get();
        }

        @Override
        public boolean disableTNT() {
            return disableTNT.get();
        }

        @Override
        public float dropChanceMultiplier() {
            return dropChanceMultiplier.get().floatValue();
        }

        @Override
        public boolean disableBlockDamage() {
            return disableBlockDamage.get();
        }

        @Override
        public boolean disableCreeperBlockDamage() {
            return disableCreeperBlockDamage.get();
        }

        @Override
        public boolean spareBlockEntities() {
            return spareBlockEntities.get();
        }

        @Override
        public boolean disableEntityDamage() {
            return disableEntityDamage.get();
        }

        @Override
        public boolean disablePlayerDamage() {
            return disablePlayerDamage.get();
        }

        @Override
        public boolean disableItemDamage() {
            return disableItemDamage.get();
        }

        @Override
        public boolean disableMobDamage() {
            return disableMobDamage.get();
        }

        @Override
        public float entityDamageMultiplier() {
            return entityDamageMultiplier.get().floatValue();
        }
    }

    static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;
    static {
        var specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
