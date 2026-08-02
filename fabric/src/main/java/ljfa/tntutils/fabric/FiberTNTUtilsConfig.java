package ljfa.tntutils.fabric;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonPrimitive;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.DecimalSerializableType;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;
import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.TNTUtilsConfigAccess;
import net.fabricmc.loader.api.FabricLoader;

public class FiberTNTUtilsConfig {
    public static class Common implements TNTUtilsConfigAccess {
        public final PropertyMirror<Boolean> addExplodeCommand = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> disableExplosions = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Float> sizeMultiplier = PropertyMirror.create(ConfigTypes.FLOAT);
        public final PropertyMirror<Boolean> preventChainExplosions = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> disableTNT = PropertyMirror.create(ConfigTypes.BOOLEAN);

        public final PropertyMirror<Float> dropChanceMultiplier = PropertyMirror.create(ConfigTypes.FLOAT);
        public final PropertyMirror<Boolean> disableBlockDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> disableCreeperBlockDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> spareBlockEntities = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Map<String, Float>> modifyExplosionResistances = PropertyMirror.create(
                ConfigTypes.makeMap(ConfigTypes.STRING, ConfigTypes.FLOAT.withMinimum(0.0f)));

        public final PropertyMirror<Boolean> disableEntityDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> disablePlayerDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> disableItemDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> disableMobDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Float> entityDamageMultiplier = PropertyMirror.create(ConfigTypes.FLOAT);

        public ConfigTree buildConfig() {
            return ConfigTree.builder()
                    //General
                    .fork("general")
                    .withComment(GENERAL_COMMENT)

                    .beginValue("addExplodeCommand", ConfigTypes.BOOLEAN, ADD_EXPLODE_COMMAND_DEFAULT)
                    .withComment(ADD_EXPLODE_COMMAND_COMMENT + ". Requires a Minecraft restart to apply.")
                    .finishValue(addExplodeCommand::mirror)

                    .beginValue("disableExplosions", ConfigTypes.BOOLEAN, DISABLE_EXPLOSIONS_DEFAULT)
                    .withComment(DISABLE_EXPLOSIONS_COMMENT)
                    .finishValue(disableExplosions::mirror)

                    .beginValue("sizeMultiplier", ConfigTypes.FLOAT.withMinimum(SIZE_MULTIPLIER_MIN).withMaximum(SIZE_MULTIPLIER_MAX), SIZE_MULTIPLIER_DEFAULT)
                    .withComment(SIZE_MULTIPLIER_COMMENT)
                    .finishValue(sizeMultiplier::mirror)

                    .beginValue("preventChainExplosions", ConfigTypes.BOOLEAN, PREVENT_CHAIN_EXPLOSIONS_DEFAULT)
                    .withComment(PREVENT_CHAIN_EXPLOSIONS_COMMENT)
                    .finishValue(preventChainExplosions::mirror)

                    .beginValue("disableTNT", ConfigTypes.BOOLEAN, DISABLE_TNT_DEFAULT)
                    .withComment(DISABLE_TNT_COMMENT)
                    .finishValue(disableTNT::mirror)

                    .finishBranch()
                    //Block damage
                    .fork("blockDamage")
                    .withComment(BLOCK_DAMAGE_COMMENT)

                    .beginValue("dropChanceMultiplier", ConfigTypes.FLOAT.withMinimum(DROP_CHANCE_MULTIPLIER_MIN), DROP_CHANCE_MULTIPLIER_DEFAULT)
                    .withComment(DROP_CHANCE_MULTIPLIER_COMMENT)
                    .finishValue(dropChanceMultiplier::mirror)

                    .beginValue("disableBlockDamage", ConfigTypes.BOOLEAN, DISABLE_BLOCK_DAMAGE_DEFAULT)
                    .withComment(DISABLE_BLOCK_DAMAGE_COMMENT)
                    .finishValue(disableBlockDamage::mirror)

                    .beginValue("disableCreeperBlockDamage", ConfigTypes.BOOLEAN, DISABLE_CREEPER_BLOCK_DAMAGE_DEFAULT)
                    .withComment(DISABLE_CREEPER_BLOCK_DAMAGE_COMMENT)
                    .finishValue(disableCreeperBlockDamage::mirror)

                    .beginValue("spareBlockEntities", ConfigTypes.BOOLEAN, SPARE_BLOCK_ENTITIES_DEFAULT)
                    .withComment(SPARE_BLOCK_ENTITIES_COMMENT)
                    .finishValue(spareBlockEntities::mirror)

                    .beginValue("modifyExplosionResistances", modifyExplosionResistances.getMirroredType(), Map.of())
                    .withComment(MODIFY_EXPLOSION_RESISTANCES_COMMENT
                            + "\nThis is an object of entries of the form \"mod_id:block_id\": value"
                            + "\nRequires a Minecraft restart to apply.")
                    .finishValue(modifyExplosionResistances::mirror)

                    .finishBranch()
                    //Entity damage
                    .fork("entityDamage")
                    .withComment(ENTITY_DAMAGE_COMMENT)

                    .beginValue("disableEntityDamage", ConfigTypes.BOOLEAN, DISABLE_ENTITY_DAMAGE_DEFAULT)
                    .withComment(DISABLE_ENTITY_DAMAGE_COMMENT)
                    .finishValue(disableEntityDamage::mirror)

                    .beginValue("disablePlayerDamage", ConfigTypes.BOOLEAN, DISABLE_PLAYER_DAMAGE_DEFAULT)
                    .withComment(DISABLE_PLAYER_DAMAGE_COMMENT)
                    .finishValue(disablePlayerDamage::mirror)

                    .beginValue("disableItemDamage", ConfigTypes.BOOLEAN, DISABLE_ITEM_DAMAGE_DEFAULT)
                    .withComment(DISABLE_ITEM_DAMAGE_COMMENT)
                    .finishValue(disableItemDamage::mirror)

                    .beginValue("disableMobDamage", ConfigTypes.BOOLEAN, DISABLE_MOB_DAMAGE_DEFAULT)
                    .withComment(DISABLE_MOB_DAMAGE_COMMENT)
                    .finishValue(disableMobDamage::mirror)

                    .beginValue("entityDamageMultiplier", ConfigTypes.FLOAT.withMinimum(ENTITY_DAMAGE_MULTIPLIER_MIN), ENTITY_DAMAGE_MULTIPLIER_DEFAULT)
                    .withComment(ENTITY_DAMAGE_MULTIPLIER_COMMENT)
                    .finishValue(entityDamageMultiplier::mirror)

                    .finishBranch()
                    .build();
        }

        @Override
        public boolean addExplodeCommand() {
            return addExplodeCommand.getValue();
        }

        @Override
        public boolean disableExplosions() {
            return disableExplosions.getValue();
        }

        @Override
        public float sizeMultiplier() {
            return sizeMultiplier.getValue();
        }

        @Override
        public boolean preventChainExplosions() {
            return preventChainExplosions.getValue();
        }

        @Override
        public boolean disableTNT() {
            return disableTNT.getValue();
        }

        @Override
        public float dropChanceMultiplier() {
            return dropChanceMultiplier.getValue();
        }

        @Override
        public boolean disableBlockDamage() {
            return disableBlockDamage.getValue();
        }

        @Override
        public boolean disableCreeperBlockDamage() {
            return disableCreeperBlockDamage.getValue();
        }

        @Override
        public boolean spareBlockEntities() {
            return spareBlockEntities.getValue();
        }

        @Override
        public boolean disableEntityDamage() {
            return disableEntityDamage.getValue();
        }

        @Override
        public boolean disablePlayerDamage() {
            return disablePlayerDamage.getValue();
        }

        @Override
        public boolean disableItemDamage() {
            return disableItemDamage.getValue();
        }

        @Override
        public boolean disableMobDamage() {
            return disableMobDamage.getValue();
        }

        @Override
        public float entityDamageMultiplier() {
            return entityDamageMultiplier.getValue();
        }
    }

    public static final Common COMMON = new Common();

    private static final ConfigTree configTree = COMMON.buildConfig();
    private static final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("tntutils.json5");

    public static void init() {
        var serializer = new FixedJanksonValueSerializer(false);

        //try reading the config file
        try(var reader = new BufferedInputStream(Files.newInputStream(configFile))) {
            FiberSerialization.deserialize(configTree, reader, serializer);
        }
        catch (NoSuchFileException e) {
            TNTUtils.logger.info("Creating new config file");
        }
        catch (IOException | ValueDeserializationException e) {
            TNTUtils.logger.error("Error reading config file, will create a backup", e);
            var timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            var backupFile = configFile.resolveSibling("tntutils-" + timestamp + ".json5.bak");
            try {
                Files.move(configFile, backupFile);
            }
            catch(IOException e1) {
                TNTUtils.logger.error("Error backing up config file", e1);
                return; //don't write if backup failed
            }
        }

        //write the config file
        //TODO: Can we avoid writing the config from scratch if nothing has changed? We should introduce a version number for the config and only write if the config needs to be migrated.
        try(var writer = new BufferedOutputStream(Files.newOutputStream(configFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            //manually prepend a comment since FiberSerialization doesn't write top-level comments
            writer.write("// This configuration file can be reloaded in-game with the '/reload-tntutils-config' command.\n".getBytes(StandardCharsets.UTF_8));
            FiberSerialization.serialize(configTree, writer, serializer);
        }
        catch (IOException e) {
            TNTUtils.logger.error("Error writing config file", e);
        }
    }

    public static void reload() throws Exception {
        var serializer = new FixedJanksonValueSerializer(false);
        try(var reader = new BufferedInputStream(Files.newInputStream(configFile))) {
            FiberSerialization.deserialize(configTree, reader, serializer);
        }
    }

    /* HACK: This works around an issue in Jankson 1.2.1+ where BigDecimals are silently truncated to longs on serialization.
     * Note that Fiber includes Jankson 1.2.0, so this problem only surfaces when another mod includes a newer version of Jankson,
     * see https://github.com/ljfa-ag/TNTUtils/issues/12.
     */
    private static class FixedJanksonValueSerializer extends JanksonValueSerializer {
        public FixedJanksonValueSerializer(boolean minify) {
            super(minify);
        }

        @Override
        public JsonElement serializeNumber(BigDecimal value, DecimalSerializableType type) {
            // We only use float values, no integers, so we can serialize everything as decimal numbers.
            return new JsonPrimitive(value.doubleValue());
        }
    }
}
