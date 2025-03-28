package net.minestom.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
    BLOCK_SOUND_TYPES("block_sound_types", new BlockSoundTypeGenerator()),
    CUSTOM_STATISTICS("custom_statistics", new CustomStatisticGenerator()),
    DYE_COLORS("dye_colors", new DyeColorGenerator()),
    ENTITIES("entities", new EntityGenerator()),
    FEATURE_FLAGS("feature_flags", new FeatureFlagGenerator()),
    FLUIDS("fluids", new FluidGenerator()),
    GAME_EVENTS("game_events", new GameEventGenerator()),
    WORLD_EVENTS("world_events", new WorldEventGenerator()),
    MATERIALS("items", new MaterialGenerator()),
    MAP_COLORS("map_colors", new MapColorGenerator()),
    PARTICLES("particles", new ParticleGenerator()),
    MOB_EFFECTS("potion_effects", new MobEffectGenerator()),
    POTIONS("potions", new PotionGenerator()),
    SOUNDS("sounds", new SoundGenerator()),
    SOUND_SOURCES("sound_sources", new SoundSourceGenerator()),
    VILLAGER_PROFESSIONS("villager_professions", new VillagerProfessionGenerator()),
    VILLAGER_TYPES("villager_types", new GenericRegistryGenerator<>(BuiltInRegistries.VILLAGER_TYPE)),
    RECIPE_TYPE("recipe_types", new GenericRegistryGenerator<>(BuiltInRegistries.RECIPE_TYPE)),
    RECIPE_DISPLAY_TYPE("recipe_display_types", new GenericRegistryGenerator<>(BuiltInRegistries.RECIPE_DISPLAY)),
    SLOT_DISPLAY_TYPE("slot_display_types", new GenericRegistryGenerator<>(BuiltInRegistries.SLOT_DISPLAY)),
    RECIPE_BOOK_CATEGORY("recipe_book_categories", new GenericRegistryGenerator<>(BuiltInRegistries.RECIPE_BOOK_CATEGORY)),
    CONSUME_EFFECT("consume_effects", new GenericRegistryGenerator<>(BuiltInRegistries.CONSUME_EFFECT_TYPE)),
    COMMAND_ARGUMENTS("command_arguments", new GenericRegistryGenerator<>(BuiltInRegistries.COMMAND_ARGUMENT_TYPE)),

    // Tags are specified as a special case in datagen

    DIMENSION_TYPES("dimension_types", new GenericResourceGenerator("dimension_type")),
    CHAT_TYPES("chat_types", new GenericResourceGenerator("chat_type")),
    DAMAGE_TYPES("damage_types", new GenericResourceGenerator("damage_type")),
    BANNER_PATTERNS("banner_patterns", new GenericResourceGenerator("banner_pattern")),
    WOLF_VARIANTS("wolf_variants", new GenericResourceGenerator("wolf_variant")),
    TRIM_MATERIALS("trim_materials", new GenericResourceGenerator("trim_material")),
    TRIM_PATTERNS("trim_patterns", new GenericResourceGenerator("trim_pattern")),
    ENCHANTMENTS("enchantments", new GenericResourceGenerator("enchantment")),
    PAINTING_VARIANTS("painting_variants", new GenericResourceGenerator("painting_variant")),
    JUKEBOX_SONGS("jukebox_songs", new GenericResourceGenerator("jukebox_song")),
    INSTRUMENTS("instruments", new GenericResourceGenerator("instrument")),
    CAT_VARIANTS("cat_variants", new GenericResourceGenerator("cat_variant")),
    CHICKEN_VARIANTS("chicken_variants", new GenericResourceGenerator("chicken_variant")),
    COW_VARIANTS("cow_variants", new GenericResourceGenerator("cow_variant")),
    FROG_VARIANTS("frog_variants", new GenericResourceGenerator("frog_variant")),
    PIG_VARIANTS("pig_variants", new GenericResourceGenerator("pig_variant")),
    WOLF_SOUND_VARIANTS("wolf_sound_variants", new GenericResourceGenerator("wolf_sound_variant")),

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