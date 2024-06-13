package net.minestom.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minestom.generators.tags.GenericTagGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class DataGen {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGen.class);
    private static Path OUTPUT = Path.of("../MinestomData/");

    private static final List<String> TAG_TYPES = List.of("banner_pattern", "block", "cat_variant", "damage_type", "enchantment", "entity_type", "fluid", "game_event", "instrument", "item", "painting_variant", "worldgen/biome");

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            OUTPUT = Path.of(args[0]);
        }
        if (!Files.exists(OUTPUT)) {
            Files.createDirectories(OUTPUT);
        }

        LOGGER.info("Generation starting...");
        for (var type : DataGenType.values()) {
            generate(type.getFileName(), type.getGenerator());
        }
        for (var tag : TAG_TYPES) {
            if (tag.contains("/")) { // Slice off worldgen from worldgen/biome
                tag = tag.substring(tag.lastIndexOf("/") + 1);
            }
            generate("tags/" + tag, new GenericTagGenerator(tag));
        }
        LOGGER.info("Generation done!");
    }

    public static void generate(String fileName, DataGenerator generator) {
        try {
            Object result = generator.generate();
            final var path = OUTPUT.resolve(fileName + (result instanceof JsonElement ? ".json" : ".snbt"));

            // Ensure that the directory exists
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                try {
                    if (result instanceof JsonElement) {
                        GSON.toJson(result, writer);
                    } else {
                        writer.write(result.toString());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
