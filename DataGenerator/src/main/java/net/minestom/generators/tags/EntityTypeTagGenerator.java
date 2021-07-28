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

public final class EntityTypeTagGenerator extends DataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityTypeTagGenerator.class);

    @Override
    public JsonObject generate() {
        File entityTypeTagsFolder = TAGS_FOLDER.resolve("entity_types").toFile();
        File[] listedFiles = entityTypeTagsFolder.listFiles();
        if (listedFiles != null) {
            List<File> children = new ArrayList<>(Arrays.asList(listedFiles));
            JsonObject entityTypeTags = new JsonObject();
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
                JsonObject entityTypeTag;
                try {
                    entityTypeTag = DataGen.GSON.fromJson(new JsonReader(new FileReader(file)), JsonObject.class);
                } catch (FileNotFoundException e) {
                    LOGGER.error("Failed to read entity type tag located at '" + file + "'.", e);
                    continue;
                }
                String fileName = file.getAbsolutePath().substring(entityTypeTagsFolder.getAbsolutePath().length() + 1);
                // Make sure we use the correct slashes.
                fileName = fileName.replace("\\", "/");
                // Remove .json (substring of 5)
                String tagName = fileName.substring(0, fileName.length() - 5);

                entityTypeTags.add("minecraft:" + tagName, entityTypeTag);
            }
            return entityTypeTags;
        } else {
            LOGGER.error("Failed to find entity type tags in data folder.");
            return new JsonObject();
        }
    }
}
