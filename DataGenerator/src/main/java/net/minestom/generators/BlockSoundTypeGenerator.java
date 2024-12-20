package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.level.block.SoundType;
import net.minestom.datagen.DataGenerator;

import java.lang.reflect.Modifier;

public class BlockSoundTypeGenerator extends DataGenerator {

    @Override
    public JsonArray generate() {
        JsonArray blockSoundTypes = new JsonArray();

        try {
            for (var field : SoundType.class.getDeclaredFields()) {
                if ((field.getModifiers() & Modifier.STATIC) == 0) continue;

                SoundType soundType = (SoundType) field.get(null);
                String name = field.getName().toLowerCase();
                JsonObject soundTypeJson = new JsonObject();
                soundTypeJson.addProperty("name", name);
                soundTypeJson.addProperty("volume", soundType.volume);
                soundTypeJson.addProperty("pitch", soundType.pitch);
                soundTypeJson.addProperty("breakSound", soundType.getBreakSound().location().toString());
                soundTypeJson.addProperty("hitSound", soundType.getHitSound().location().toString());
                soundTypeJson.addProperty("fallSound", soundType.getFallSound().location().toString());
                soundTypeJson.addProperty("placeSound", soundType.getPlaceSound().location().toString());
                soundTypeJson.addProperty("stepSound", soundType.getStepSound().location().toString());
                blockSoundTypes.add(soundTypeJson);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return blockSoundTypes;
    }
}
