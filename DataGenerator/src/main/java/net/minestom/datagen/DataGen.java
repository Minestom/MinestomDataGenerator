package net.minestom.datagen;

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

public class DataGen {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenHolder.class);

    private DataGen() {
    }

    public static void main(String[] args) {
        DataGeneratorCommon.prepare();
        DataGenHolder.addGenerator(DataGenType.ATTRIBUTES, new AttributeGenerator());
        DataGenHolder.addGenerator(DataGenType.BIOMES, new BiomeGenerator());
        DataGenHolder.addGenerator(DataGenType.BLOCKS, new BlockGenerator());
        DataGenHolder.addGenerator(DataGenType.CUSTOM_STATISTICS, new CustomStatisticGenerator());
        DataGenHolder.addGenerator(DataGenType.DIMENSION_TYPES, new DimensionTypeGenerator());
        DataGenHolder.addGenerator(DataGenType.ENCHANTMENTS, new EnchantmentGenerator());
        DataGenHolder.addGenerator(DataGenType.ENTITIES, new EntityGenerator());
        DataGenHolder.addGenerator(DataGenType.FLUIDS, new FluidGenerator());
        DataGenHolder.addGenerator(DataGenType.GAME_EVENTS, new GameEventGenerator());
        DataGenHolder.addGenerator(DataGenType.MATERIALS, new MaterialGenerator());
        DataGenHolder.addGenerator(DataGenType.MAP_COLORS, new MapColorGenerator());
        DataGenHolder.addGenerator(DataGenType.PARTICLES, new ParticleGenerator());
        DataGenHolder.addGenerator(DataGenType.MOB_EFFECTS, new MobEffectGenerator());
        DataGenHolder.addGenerator(DataGenType.POTIONS, new PotionGenerator());
        DataGenHolder.addGenerator(DataGenType.SOUND_SOURCES, new SoundSourceGenerator());
        DataGenHolder.addGenerator(DataGenType.SOUNDS, new SoundGenerator());
        DataGenHolder.addGenerator(DataGenType.VILLAGER_PROFESSIONS, new VillagerProfessionGenerator());
        DataGenHolder.addGenerator(DataGenType.VILLAGER_TYPES, new VillagerTypeGenerator());

        DataGenHolder.addGenerator(DataGenType.BLOCK_TAGS, new BlockTagGenerator());
        DataGenHolder.addGenerator(DataGenType.ENTITY_TYPE_TAGS, new EntityTypeTagGenerator());
        DataGenHolder.addGenerator(DataGenType.FLUID_TAGS, new FluidTagGenerator());
        DataGenHolder.addGenerator(DataGenType.ITEM_TAGS, new ItemTagGenerator());
        DataGenHolder.addGenerator(DataGenType.GAMEEVENT_TAGS, new GameEventTagGenerator());

        DataGenHolder.addGenerator(DataGenType.BLOCK_LOOT_TABLES, new BlockLootTableGenerator());
        DataGenHolder.addGenerator(DataGenType.CHEST_LOOT_TABLES, new ChestLootTableGenerator());
        DataGenHolder.addGenerator(DataGenType.ENTITY_LOOT_TABLES, new EntityLootTableGenerator());
        DataGenHolder.addGenerator(DataGenType.GAMEPLAY_LOOT_TABLES, new GameplayLootTableGenerator());

        // Folder for the output.
        // Remove a character at the end since the prefix includes an _ at the end
        File outputFolder = new File("../MinestomData/");
        if (args.length >= 1) {
            outputFolder = new File(args[0]);
        }
        DataGenHolder.runGenerators(new JsonOutputter(outputFolder));

        LOGGER.info("Output data in: " + outputFolder.getAbsolutePath());
    }
}
