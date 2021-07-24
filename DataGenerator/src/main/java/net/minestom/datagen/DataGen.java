package net.minestom.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minestom.generators.*;
import net.minestom.generators.common.DataGeneratorCommon;
import net.minestom.generators.loot_tables.BlockLootTableGenerator;
import net.minestom.generators.loot_tables.ChestLootTableGenerator;
import net.minestom.generators.loot_tables.EntityLootTableGenerator;
import net.minestom.generators.loot_tables.GameplayLootTableGenerator;
import net.minestom.generators.tags.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DataGen {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGen.class);
    private static final Path OUTPUT = Path.of("../MinestomData/");

    private DataGen() {
    }

    public static void main(String[] args) {
        DataGeneratorCommon.prepare();
        generate(DataGenType.ATTRIBUTES, new AttributeGenerator());
        generate(DataGenType.BIOMES, new BiomeGenerator());
        generate(DataGenType.BLOCKS, new BlockGenerator());
        generate(DataGenType.CUSTOM_STATISTICS, new CustomStatisticGenerator());
        generate(DataGenType.DIMENSION_TYPES, new DimensionTypeGenerator());
        generate(DataGenType.ENCHANTMENTS, new EnchantmentGenerator());
        generate(DataGenType.ENTITIES, new EntityGenerator());
        generate(DataGenType.FLUIDS, new FluidGenerator());
        generate(DataGenType.GAME_EVENTS, new GameEventGenerator());
        generate(DataGenType.MATERIALS, new MaterialGenerator());
        generate(DataGenType.MAP_COLORS, new MapColorGenerator());
        generate(DataGenType.PARTICLES, new ParticleGenerator());
        generate(DataGenType.MOB_EFFECTS, new MobEffectGenerator());
        generate(DataGenType.POTIONS, new PotionGenerator());
        generate(DataGenType.SOUND_SOURCES, new SoundSourceGenerator());
        generate(DataGenType.SOUNDS, new SoundGenerator());
        generate(DataGenType.VILLAGER_PROFESSIONS, new VillagerProfessionGenerator());
        generate(DataGenType.VILLAGER_TYPES, new VillagerTypeGenerator());

        generate(DataGenType.BLOCK_TAGS, new BlockTagGenerator());
        generate(DataGenType.ENTITY_TYPE_TAGS, new EntityTypeTagGenerator());
        generate(DataGenType.FLUID_TAGS, new FluidTagGenerator());
        generate(DataGenType.ITEM_TAGS, new ItemTagGenerator());
        generate(DataGenType.GAMEEVENT_TAGS, new GameEventTagGenerator());

        generate(DataGenType.BLOCK_LOOT_TABLES, new BlockLootTableGenerator());
        generate(DataGenType.CHEST_LOOT_TABLES, new ChestLootTableGenerator());
        generate(DataGenType.ENTITY_LOOT_TABLES, new EntityLootTableGenerator());
        generate(DataGenType.GAMEPLAY_LOOT_TABLES, new GameplayLootTableGenerator());

        LOGGER.info("Generation done!");
    }

    public static void generate(DataGenType type, DataGenerator generator) {
        final var path = OUTPUT.resolve(type.getFileName() + ".json");
        try {
            // Ensure that the directory exists
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                GSON.toJson(generator.generate(), writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
