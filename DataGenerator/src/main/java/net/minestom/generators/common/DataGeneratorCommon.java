package net.minestom.generators.common;

import net.minecraft.SharedConstants;
import net.minecraft.data.Main;
import net.minecraft.server.Bootstrap;
import net.minestom.datagen.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class DataGeneratorCommon implements DataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGeneratorCommon.class);
    protected static File dataFolder;

    public static void prepare() {
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
            return;
        }
        // Points to data/minecraft
        dataFolder = new File(tempDirFile, "data" + File.separator + "minecraft");
    }
}
