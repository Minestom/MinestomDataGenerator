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
    // Tags are specified as a special case in datagen

    // Codegen only

    COMMAND_ARGUMENTS("command_arguments", new GenericRegistryGenerator<>(BuiltInRegistries.COMMAND_ARGUMENT_TYPE)),
    CONSUME_EFFECT("consume_effects", new GenericRegistryGenerator<>(BuiltInRegistries.CONSUME_EFFECT_TYPE)),
    CUSTOM_STATISTICS("custom_statistics", new CustomStatisticGenerator()),
    DYE_COLORS("dye_colors", new DyeColorGenerator()),
    MAP_COLORS("map_colors", new MapColorGenerator()),
    PARTICLES("particle", new ParticleGenerator()),
    WORLD_EVENTS("world_events", new WorldEventGenerator()),
    RECIPE_BOOK_CATEGORY("recipe_book_categories", new GenericRegistryGenerator<>(BuiltInRegistries.RECIPE_BOOK_CATEGORY)),
    RECIPE_DISPLAY_TYPE("recipe_display_types", new GenericRegistryGenerator<>(BuiltInRegistries.RECIPE_DISPLAY)),
    RECIPE_TYPE("recipe_types", new GenericRegistryGenerator<>(BuiltInRegistries.RECIPE_TYPE)),
    SLOT_DISPLAY_TYPE("slot_display_types", new GenericRegistryGenerator<>(BuiltInRegistries.SLOT_DISPLAY)),
    SOUND_SOURCES("sound_sources", new SoundSourceGenerator()),
    VILLAGER_TYPES("villager_types", new GenericRegistryGenerator<>(BuiltInRegistries.VILLAGER_TYPE)),

    // Static registries

    ATTRIBUTES("attribute", new AttributeGenerator()),
    BLOCKS("block", new BlockGenerator()),
    BLOCK_SOUND_TYPES("block_sound_type", new BlockSoundTypeGenerator()),
    DEFAULT_ATTRIBUTES("default_attributes", new EntityAttributeGenerator()),
    ENTITIES("entity_type", new EntityGenerator()),
    FEATURE_FLAGS("feature_flag", new FeatureFlagGenerator()),
    FLUIDS("fluid", new FluidGenerator()),
    GAME_EVENTS("game_event", new GameEventGenerator()),
    MATERIALS("item", new MaterialGenerator()),
    MOB_EFFECTS("potion_effect", new MobEffectGenerator()),
    POTIONS("potion_type", new PotionGenerator()),
    SOUNDS("sound_event", new SoundGenerator()),
    VILLAGER_PROFESSIONS("villager_profession", new VillagerProfessionGenerator()),

    // Dynamic Registries

    BANNER_PATTERNS("banner_pattern", new GenericResourceGenerator("banner_pattern")),
    BIOMES("worldgen/biome", new BiomeGenerator()),
    CAT_VARIANTS("cat_variant", new GenericResourceGenerator("cat_variant")),
    CHAT_TYPES("chat_type", new GenericResourceGenerator("chat_type")),
    CHICKEN_VARIANTS("chicken_variant", new GenericResourceGenerator("chicken_variant")),
    COW_VARIANTS("cow_variant", new GenericResourceGenerator("cow_variant")),
    DAMAGE_TYPES("damage_type", new GenericResourceGenerator("damage_type")),
    DIALOGS("dialog", new GenericResourceGenerator("dialog")),
    DIMENSION_TYPES("dimension_type", new GenericResourceGenerator("dimension_type")),
    ENCHANTMENTS("enchantment", new GenericResourceGenerator("enchantment")),
    FROG_VARIANTS("frog_variant", new GenericResourceGenerator("frog_variant")),
    JUKEBOX_SONGS("jukebox_song", new GenericResourceGenerator("jukebox_song")),
    INSTRUMENTS("instrument", new GenericResourceGenerator("instrument")),
    PAINTING_VARIANTS("painting_variant", new GenericResourceGenerator("painting_variant")),
    PIG_VARIANTS("pig_variant", new GenericResourceGenerator("pig_variant")),
    TRIM_MATERIALS("trim_material", new GenericResourceGenerator("trim_material")),
    TRIM_PATTERNS("trim_pattern", new GenericResourceGenerator("trim_pattern")),
    WOLF_VARIANTS("wolf_variant", new GenericResourceGenerator("wolf_variant")),
    WOLF_SOUND_VARIANTS("wolf_sound_variant", new GenericResourceGenerator("wolf_sound_variant")),

    // Loot tables (only included for legacy reasons, Minestom doesn't use them)

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