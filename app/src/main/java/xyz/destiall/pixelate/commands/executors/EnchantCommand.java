package xyz.destiall.pixelate.commands.executors;

import xyz.destiall.pixelate.commands.Command;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.meta.Enchantment;
import xyz.destiall.pixelate.items.meta.ItemMeta;

/**
 * Written by Rance
 */
public class EnchantCommand implements Command {
    @Override
    public boolean onCommand(EntityPlayer player, String command, String[] args) {
        if (args.length == 0) return false;
        ItemStack hand = player.getItemInHand();
        if (hand == null) return false;
        ItemMeta meta = hand.getItemMeta();
        String type = args[0];
        Enchantment enchantment = Enchantment.getFromName(type);
        if (enchantment == null) return false;
        if (meta.hasEnchantment(enchantment)) return false;

        int level = 1;
        if (args.length > 1) {
            try {
                level = Integer.parseInt(args[1]);
            } catch (Exception ignored) {}
        }

        if (level > enchantment.getMaxLevel()) return false;
        meta.addEnchantment(enchantment, level);
        return true;
    }
}
