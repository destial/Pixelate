package xyz.destiall.pixelate.serialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import xyz.destiall.java.reflection.Reflect;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.inventory.Inventory;

/**
 * Written by Rance
 */
public class InventorySerializer implements JsonSerializer<Inventory>, JsonDeserializer<Inventory> {
    @Override
    public JsonElement serialize(Inventory src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", src.getClass().getCanonicalName());
        obj.add("data", context.serialize(src, src.getClass()));
        return obj;
    }

    @Override
    public Inventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String clazz = obj.get("type").getAsString();
        JsonElement data = obj.get("data");
        Class<?> enClass = Reflect.getClass(clazz);
        if (enClass == null) return null;
        Inventory inventory = context.deserialize(data, enClass);
        Method set = Reflect.getMethod(Inventory.class, "setItemStackInventory");
        set.setAccessible(true);
        for (ItemStack stack : inventory.getItems()) {
            try {
                if (stack == null) continue;
                set.invoke(null, stack, inventory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        set.setAccessible(false);
        return inventory;
    }
}
