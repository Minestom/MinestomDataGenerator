package net.minestom.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.SharedConstants;
import net.minecraft.data.Main;
import net.minecraft.server.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class DataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);
    protected static final File DATA_FOLDER;

    static {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        // Create a temp file, run Mojang's data generator and "recompile" that data.
        File tempDirFile;
        try {
            tempDirFile = Files.createTempDirectory("mojang_gen_data").toFile();
            Main.main(new String[]{
                    "--all",
                    "--output=" + tempDirFile
            });
            // Delete tempFile when finished
            tempDirFile.deleteOnExit();
        } catch (IOException e) {
            LOGGER.error("Something went wrong while running Mojang's data generator.", e);
            throw new RuntimeException("Couldn't run the generator");
        }
        // Points to data/minecraft
        DATA_FOLDER = new File(tempDirFile, "data" + File.separator + "minecraft");
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
}
