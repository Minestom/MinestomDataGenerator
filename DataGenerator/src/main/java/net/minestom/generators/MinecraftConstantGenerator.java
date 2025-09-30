package net.minestom.generators;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackType;
import net.minestom.datagen.DataGenerator;

public class MinecraftConstantGenerator extends DataGenerator {

    @Override
    public JsonElement generate() throws Exception {
        var version = SharedConstants.getCurrentVersion();
        JsonObject obj = new JsonObject();
        obj.addProperty("name", version.id());
        obj.addProperty("friendly_name", version.name());
        obj.addProperty("protocol", version.protocolVersion());
        obj.addProperty("world", version.dataVersion().version());
        obj.addProperty("resourcepack", version.packVersion(PackType.CLIENT_RESOURCES).toString());
        obj.addProperty("datapack", version.packVersion(PackType.SERVER_DATA).toString());
        return obj;
    }

}
