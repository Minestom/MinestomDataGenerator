package net.minestom.datagen;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minecraft.SharedConstants;
import net.minecraft.data.Main;
import net.minecraft.server.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
            DATA_FOLDER = tempDir.resolve("data").resolve("minecraft");
            LOOT_TABLES_FOLDER = DATA_FOLDER.resolve("loot_table");
            TAGS_FOLDER = DATA_FOLDER.resolve("tags");
        } catch (IOException e) {
            LOGGER.error("Something went wrong while running Mojang's data generator.", e);
            throw new RuntimeException("Couldn't run the generator");
        }
    }

    public abstract Object/*JsonElement, String*/ generate() throws Exception;

    protected void addDefaultable(JsonObject jsonObject, String key, boolean value, boolean defaultValue) {
        if (value != defaultValue) jsonObject.addProperty(key, value);
    }

    protected void addDefaultable(JsonObject jsonObject, String key, float value, float defaultValue) {
        if (Float.compare(value, defaultValue) != 0) jsonObject.addProperty(key, value);
    }

    protected void addDefaultable(JsonObject jsonObject, String key, double value, double defaultValue) {
        if (Double.compare(value, defaultValue) != 0) jsonObject.addProperty(key, value);
    }

    protected JsonObject mergePath(Path directory) {
        final JsonObject result = new JsonObject();
        try {
            Files.walk(directory).filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            JsonObject blockLootTable = DataGen.GSON.fromJson(new JsonReader(Files.newBufferedReader(path)), JsonObject.class);
                            final String fileName = directory.relativize(path).toString();
                            final String tableName = (File.separatorChar == '\\' ? fileName.replace('\\', '/') : fileName)
                                    .replace(".json", "");
                            result.add("minecraft:" + tableName, blockLootTable);
                        } catch (IOException e) {
                            LOGGER.error("Failed to read block loot table located at '" + path + "'.", e);
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
