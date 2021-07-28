package net.minestom.generators.loot_tables;

import com.google.gson.JsonObject;
import net.minestom.datagen.DataGenerator;

public final class BlockLootTableGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        return mergePath(LOOT_TABLES_FOLDER.resolve("blocks"));
    }
}
