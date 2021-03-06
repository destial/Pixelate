package xyz.destiall.pixelate.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.meta.Enchantment;

/**
 * Written By Yong Hong
 */
public class ViewUtils {
    private static final float DISPLAYNAME_HEADER = 0.05f;
    private static final float LORE_HEADER = 0.01f;
    private static final float LORE_SPACING = 0.035f;
    private static final float ENCHANT_HEADER = 0.035f;
    private static final float ENCHANT_SPACING = 0.035f;
    private static final float MATERIALNAME_HEADER = 0.07f;

    public static void displayItemDescription(Screen screen, Bitmap image, double drawX, double drawY, ItemStack item) {
        //Display Item
        String displayName = item.getItemMeta().getDisplayName();
        if (displayName == null) displayName = item.getType().getName();
        List<String> enchantments = new ArrayList<>();
        for (Map.Entry<Enchantment, Integer> ench : item.getItemMeta().getEnchantments().entrySet()) {
            enchantments.add(ench.getKey().getEnchantName() + " " + MathematicUtils.toRoman(ench.getValue()));
        }

        List<String> lore = item.getItemMeta().getLore();

        float loreSize = 0.05f + MATERIALNAME_HEADER + DISPLAYNAME_HEADER;
        if (lore != null && lore.size() > 0) loreSize += LORE_HEADER + LORE_SPACING * lore.size();
        if (enchantments.size() > 0) loreSize += ENCHANT_HEADER + ENCHANT_SPACING * enchantments.size();

        screen.quad(drawX + image.getWidth() / 1.1f, drawY - Pixelate.HEIGHT * loreSize, Pixelate.WIDTH * 0.21, Pixelate.HEIGHT * (loreSize + 0.01), Color.argb(255, 20,20,20));
        screen.quad(drawX + image.getWidth() / 1.2f, drawY - Pixelate.HEIGHT * 0.01 - Pixelate.HEIGHT * loreSize, Pixelate.WIDTH * 0.2, Pixelate.HEIGHT * loreSize, Color.argb(190, 76,76,76));

        double loreX = drawX + image.getWidth() / 1.2f + Pixelate.WIDTH * 0.01;
        double loreY = drawY - Pixelate.HEIGHT * 0.01 - Pixelate.HEIGHT * loreSize;

        loreY += Pixelate.HEIGHT * 0.05;
        screen.text(displayName, loreX, loreY, 40, Color.WHITE);

        if (lore != null && lore.size() > 0) {
            loreY += Pixelate.HEIGHT * 0.01;
            for (String loreLine : lore)  {
                loreY += Pixelate.HEIGHT * 0.035;
                screen.text(loreLine, loreX, loreY, 28, Color.argb(255, 203,195,227));
            }
        }

        if (enchantments.size() > 0) {
            loreY += Pixelate.HEIGHT * 0.035;
            for (String enchantLore : enchantments) {
                loreY += Pixelate.HEIGHT * 0.035;
                screen.text(enchantLore, loreX, loreY, 28, Color.BLUE);
            }
        }

        loreY += Pixelate.HEIGHT * 0.07;
        screen.text(item.getType().getName(), loreX, loreY, 28, Color.GRAY);
    }
}
