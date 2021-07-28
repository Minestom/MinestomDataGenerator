package net.minestom.generators.tags;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minestom.datagen.DataGen;
import net.minestom.datagen.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BlockTagGenerator extends DataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockTagGenerator.class);

    @Override
    public JsonObject generate() {
        File tagFolder = new File(dataFolder, "tags");
        File blockTagsFolder = new File(tagFolder, "blocks");

        File[] listedFiles = blockTagsFolder.listFiles();
        if (listedFiles != null) {
            List<File> children = new ArrayList<>(Arrays.asList(listedFiles));
            JsonObject blockTags = new JsonObject();
            for (int i = 0; i < children.size(); i++) {
                File file = children.get(i);
                // Add subdirectories files to the for-loop.
                if (file.isDirectory()) {
                    File[] subChildren = file.listFiles();
                    if (subChildren != null) {
                        children.addAll(Arrays.asList(subChildren));
                    }
                    continue;
                }
                JsonObject blockTag;
                try {
                    blockTag = DataGen.GSON.fromJson(new JsonReader(new FileReader(file)), JsonObject.class);
                } catch (FileNotFoundException e) {
                    LOGGER.error("Failed to read block tag located at '" + file + "'.", e);
                    continue;
                }
                String fileName = file.getAbsolutePath().substring(blockTagsFolder.getAbsolutePath().length() + 1);
                // Make sure we use the correct slashes.
                fileName = fileName.replace("\\", "/");
                // Remove .json (substring of 5)
                String tagName = fileName.substring(0, fileName.length() - 5);
                blockTags.add("minecraft:" + tagName, blockTag);
            }
            return blockTags;
        } else {
            LOGGER.error("Failed to find block tags in data folder.");
            return new JsonObject();
        }
    }
}
