package xyz.destiall.pixelate.commands.executors;

import xyz.destiall.pixelate.commands.Command;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.items.ItemStack;

/**
 * Written by Rance
 */
public class ItemCommand implements Command {
    @Override
    public boolean onCommand(EntityPlayer player, String command, String[] args) {
        if (args.length == 0) return false;
        String type = args[0];
        Material material = Material.getFromName(type);
        if (material == null) return false;

        int amount = 1;
        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (Exception ignored) {}
        }

        ItemStack item = new ItemStack(material, amount);
        player.getInventory().addItem(item);
        return true;
    }
}
