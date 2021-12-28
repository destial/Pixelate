package xyz.destiall.pixelate.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import xyz.destiall.java.reflection.Reflect;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.items.inventory.Inventory;

public class TileSerializer implements JsonDeserializer<Tile>, JsonSerializer<Tile> {
    private final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Inventory.class, new InventorySerializer())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    @Override
    public Tile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String typeName = jsonObject.get("type").getAsString();
        return gson.fromJson(jsonObject, (Type) Reflect.getClass(typeName));
    }

    @Override
    public JsonElement serialize(Tile src, Type typeOfSrc, JsonSerializationContext context) {
        return gson.toJsonTree(src, src.getClass()).getAsJsonObject();
    }
}
