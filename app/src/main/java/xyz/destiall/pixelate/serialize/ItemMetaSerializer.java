package xyz.destiall.pixelate.serialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import xyz.destiall.utility.java.reflection.Reflect;
import xyz.destiall.pixelate.items.meta.ItemMeta;

/**
 * Written by Rance
 */
public class ItemMetaSerializer implements JsonSerializer<ItemMeta>, JsonDeserializer<ItemMeta> {
    @Override
    public JsonElement serialize(ItemMeta src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", src.getClass().getCanonicalName());
        obj.add("data", context.serialize(src, src.getClass()));
        return obj;
    }

    @Override
    public ItemMeta deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String clazz = obj.get("type").getAsString();
        JsonElement data = obj.get("data");
        Class<?> enClass = Reflect.getClass(clazz);
        if (enClass == null) return null;
        return context.deserialize(data, enClass);
    }
}
