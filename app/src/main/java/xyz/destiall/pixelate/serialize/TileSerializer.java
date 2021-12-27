package xyz.destiall.pixelate.serialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

import xyz.destiall.java.reflection.Reflect;
import xyz.destiall.pixelate.environment.tiles.Tile;

public class TileSerializer implements JsonDeserializer<Tile> {

    @Override
    public Tile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive classNamePrimitive = (JsonPrimitive) jsonObject.get("type");
        String typeName = classNamePrimitive.getAsString();
        return context.deserialize(jsonObject, Reflect.getClass(typeName));
    }
}
