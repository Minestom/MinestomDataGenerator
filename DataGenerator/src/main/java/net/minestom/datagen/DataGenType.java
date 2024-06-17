package net.minestom.datagen;

import net.minestom.generators.*;
import net.minestom.generators.loot_tables.BlockLootTableGenerator;
import net.minestom.generators.loot_tables.ChestLootTableGenerator;
import net.minestom.generators.loot_tables.EntityLootTableGenerator;
import net.minestom.generators.loot_tables.GameplayLootTableGenerator;
import net.minestom.generators.tags.*;

import java.util.List;

public enum DataGenType {
    CONSTANTS("constants", new MinecraftConstantGenerator()),

    ATTRIBUTES("attributes", new AttributeGenerator()),
    BIOMES("biomes", new BiomeGenerator()),
    BLOCKS("blocks", new BlockGenerator()),
    COMMAND_ARGUMENTS("command_arguments", new CommandArgumentGenerator()),
    CUSTOM_STATISTICS("custom_statistics", new CustomStatisticGenerator()),
    DYE_COLORS("dye_colors", new DyeColorGenerator()),
    ENTITIES("entities", new EntityGenerator()),
    FEATURE_FLAGS("feature_flags", new FeatureFlagGenerator()),
    FLUIDS("fluids", new FluidGenerator()),
    GAME_EVENTS("game_events", new GameEventGenerator()),
    MATERIALS("items", new MaterialGenerator()),
    MAP_COLORS("map_colors", new MapColorGenerator()),
    PARTICLES("particles", new ParticleGenerator()),
    MOB_EFFECTS("potion_effects", new MobEffectGenerator()),
    POTIONS("potions", new PotionGenerator()),
    SOUNDS("sounds", new SoundGenerator()),
    SOUND_SOURCES("sound_sources", new SoundSourceGenerator()),
    VILLAGER_PROFESSIONS("villager_professions", new VillagerProfessionGenerator()),
    VILLAGER_TYPES("villager_types", new VillagerTypeGenerator()),
    RECIPE_TYPE("recipe_types", new RecipeTypeGenerator()),

    // Tags are specified as a special case in datagen

    DIMENSION_TYPES("dimension_types", new GenericResourceGenerator("dimension_type")),
    CHAT_TYPES("chat_types", new GenericResourceGenerator("chat_type")),
    DAMAGE_TYPES("damage_types", new GenericResourceGenerator("damage_type")),
    BANNER_PATTERNS("banner_patterns", new GenericResourceGenerator("banner_pattern")),
    WOLF_VARIANTS("wolf_variants", new GenericResourceGenerator("wolf_variant")),
    TRIM_MATERIALS("trim_materials", new GenericResourceGenerator("trim_material")),
    TRIM_PATTERNS("trim_patterns", new GenericResourceGenerator("trim_pattern")),
    ENCHANTMENTS("enchantments", new GenericResourceGenerator("enchantment", List.of(), true)),
    PAINTING_VARIANTS("painting_variants", new GenericResourceGenerator("painting_variant")),
    JUKEBOX_SONGS("jukebox_songs", new GenericResourceGenerator("jukebox_song")),

    BLOCK_LOOT_TABLES("loot_tables/block_loot_tables", new BlockLootTableGenerator()),
    CHEST_LOOT_TABLES("loot_tables/chest_loot_tables", new ChestLootTableGenerator()),
    ENTITY_LOOT_TABLES("loot_tables/entity_loot_tables", new EntityLootTableGenerator()),
    GAMEPLAY_LOOT_TABLES("loot_tables/gameplay_loot_tables", new GameplayLootTableGenerator());

    private final String fileName;
    private final DataGenerator generator;

    DataGenType(String fileName, DataGenerator generator) {
        this.fileName = fileName;
        this.generator = generator;
    }

    public String getFileName() {
        return fileName;
    }

    public DataGenerator getGenerator() {
        return generator;
    }
}