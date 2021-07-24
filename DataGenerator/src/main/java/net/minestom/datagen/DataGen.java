package net.minestom.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
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
import java.util.HashMap;
import java.util.Map;

public class DataGen {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGen.class);
    private static final Map<DataGenType, DataGenerator> GENERATORS = new HashMap<>();

    private DataGen() {
    }

    public static void main(String[] args) {
        DataGeneratorCommon.prepare();
        addGenerator(DataGenType.ATTRIBUTES, new AttributeGenerator());
        addGenerator(DataGenType.BIOMES, new BiomeGenerator());
        addGenerator(DataGenType.BLOCKS, new BlockGenerator());
        addGenerator(DataGenType.CUSTOM_STATISTICS, new CustomStatisticGenerator());
        addGenerator(DataGenType.DIMENSION_TYPES, new DimensionTypeGenerator());
        addGenerator(DataGenType.ENCHANTMENTS, new EnchantmentGenerator());
        addGenerator(DataGenType.ENTITIES, new EntityGenerator());
        addGenerator(DataGenType.FLUIDS, new FluidGenerator());
        addGenerator(DataGenType.GAME_EVENTS, new GameEventGenerator());
        addGenerator(DataGenType.MATERIALS, new MaterialGenerator());
        addGenerator(DataGenType.MAP_COLORS, new MapColorGenerator());
        addGenerator(DataGenType.PARTICLES, new ParticleGenerator());
        addGenerator(DataGenType.MOB_EFFECTS, new MobEffectGenerator());
        addGenerator(DataGenType.POTIONS, new PotionGenerator());
        addGenerator(DataGenType.SOUND_SOURCES, new SoundSourceGenerator());
        addGenerator(DataGenType.SOUNDS, new SoundGenerator());
        addGenerator(DataGenType.VILLAGER_PROFESSIONS, new VillagerProfessionGenerator());
        addGenerator(DataGenType.VILLAGER_TYPES, new VillagerTypeGenerator());

        addGenerator(DataGenType.BLOCK_TAGS, new BlockTagGenerator());
        addGenerator(DataGenType.ENTITY_TYPE_TAGS, new EntityTypeTagGenerator());
        addGenerator(DataGenType.FLUID_TAGS, new FluidTagGenerator());
        addGenerator(DataGenType.ITEM_TAGS, new ItemTagGenerator());
        addGenerator(DataGenType.GAMEEVENT_TAGS, new GameEventTagGenerator());

        addGenerator(DataGenType.BLOCK_LOOT_TABLES, new BlockLootTableGenerator());
        addGenerator(DataGenType.CHEST_LOOT_TABLES, new ChestLootTableGenerator());
        addGenerator(DataGenType.ENTITY_LOOT_TABLES, new EntityLootTableGenerator());
        addGenerator(DataGenType.GAMEPLAY_LOOT_TABLES, new GameplayLootTableGenerator());

        // Folder for the output.
        // Remove a character at the end since the prefix includes an _ at the end
        final var outputPath = Path.of("../MinestomData/");
        // Run generators
        for (var entry : GENERATORS.entrySet()) {
            DataGenType type = entry.getKey();
            DataGenerator generator = entry.getValue();

            JsonElement data = generator.generate();
            write(outputPath.resolve(type.getFileName() + ".json"), data);
        }
        LOGGER.info("Output data in: " + outputPath.getFileName());
    }

    public static void addGenerator(DataGenType type, DataGenerator dg) {
        GENERATORS.put(type, dg);
    }

    private static void write(Path path, JsonElement output) {
        try {
            // Ensure that the directory exists
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                GSON.toJson(output, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
