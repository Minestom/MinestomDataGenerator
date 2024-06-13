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
        obj.addProperty("name", version.getId());
        obj.addProperty("friendly_name", version.getName());
        obj.addProperty("protocol", version.getProtocolVersion());
        obj.addProperty("world", version.getDataVersion().getVersion());
        obj.addProperty("resourcepack", version.getPackVersion(PackType.CLIENT_RESOURCES));
        obj.addProperty("datapack", version.getPackVersion(PackType.SERVER_DATA));
        return obj;
    }

}
