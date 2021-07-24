package net.minestom.datagen;

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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DataGen {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGen.class);
    private static final Map<DataGenType, DataGenerator> generators = new HashMap<>();

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
        File outputFolder = new File("../MinestomData/");
        if (args.length >= 1) {
            outputFolder = new File(args[0]);
        }
        // Run generators
        var output = new JsonOutputter(outputFolder);
        for (var entry : generators.entrySet()) {
            DataGenType type = entry.getKey();
            DataGenerator generator = entry.getValue();

            JsonElement data = generator.generate();
            output.output(data, type.getFileName());
        }
        LOGGER.info("Output data in: " + outputFolder.getAbsolutePath());
    }

    public static void addGenerator(DataGenType type, DataGenerator dg) {
        generators.put(type, dg);
    }
}
