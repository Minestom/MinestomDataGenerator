package net.minestom.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minecraft.SharedConstants;
import net.minecraft.data.Main;
import net.minecraft.server.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public abstract class DataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);
    protected static final Path DATA_FOLDER;
    protected static final Path LOOT_TABLES_FOLDER;
    protected static final Path TAGS_FOLDER;

    static {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        // Create a temp file, run Mojang's data generator and "recompile" that data.
        Path tempDir;
        try {
            tempDir = Files.createTempDirectory("mojang_gen_data");
            Main.main(new String[]{
                    "--all",
                    "--output=" + tempDir
            });
        } catch (IOException e) {
            LOGGER.error("Something went wrong while running Mojang's data generator.", e);
            throw new RuntimeException("Couldn't run the generator");
        }
        // Points to data/minecraft
        DATA_FOLDER = tempDir.resolve("data").resolve("minecraft");
        LOOT_TABLES_FOLDER = DATA_FOLDER.resolve("loot_tables");
        TAGS_FOLDER = DATA_FOLDER.resolve("tags");
    }

    public abstract JsonElement generate();

    protected void addDefaultable(JsonObject jsonObject, String key, boolean value, boolean defaultValue) {
        if (value != defaultValue) {
            jsonObject.addProperty(key, value);
        }
    }

    protected void addDefaultable(JsonObject jsonObject, String key, float value, float defaultValue) {
        if (Float.compare(value, defaultValue) != 0) {
            jsonObject.addProperty(key, value);
        }
    }

    protected JsonObject mergePath(Path directory) {
        return mergePath(null, directory);
    }

    private JsonObject mergePath(JsonObject object, Path directory) {
        final JsonObject result = Objects.requireNonNullElseGet(object, JsonObject::new);
        try {
            Files.list(directory).forEach(path -> {
                if (Files.isDirectory(path)) {
                    mergePath(result, path);
                } else {
                    JsonObject blockLootTable;
                    try {
                        blockLootTable = DataGen.GSON.fromJson(new JsonReader(Files.newBufferedReader(path)), JsonObject.class);
                    } catch (IOException e) {
                        LOGGER.error("Failed to read block loot table located at '" + path + "'.", e);
                        return;
                    }
                    String tableName = path.getFileName().toString().replace(".json", "");
                    result.add("minecraft:" + tableName, blockLootTable);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
