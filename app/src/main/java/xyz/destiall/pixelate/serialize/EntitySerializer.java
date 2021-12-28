package xyz.destiall.pixelate.serialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import xyz.destiall.java.reflection.Reflect;
import xyz.destiall.pixelate.entities.Entity;

public class EntitySerializer implements JsonSerializer<Entity>, JsonDeserializer<Entity> {
    @Override
    public JsonElement serialize(Entity src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", src.getClass().getCanonicalName());
        obj.add("data", context.serialize(src, src.getClass()));
        return obj;
    }

    @Override
    public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String clazz = obj.get("type").getAsString();
        JsonElement data = obj.get("data");
        Class<?> enClass = Reflect.getClass(clazz);
        if (enClass == null) return null;
        Entity entity = context.deserialize(data, enClass);
        entity.refresh();
        return entity;
    }
}
