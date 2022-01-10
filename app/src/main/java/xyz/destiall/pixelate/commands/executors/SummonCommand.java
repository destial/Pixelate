package xyz.destiall.pixelate.commands.executors;

import java.util.Arrays;

import xyz.destiall.pixelate.commands.Command;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.entities.EntityPrimedTNT;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.items.ItemStack;

/**
 * Written by Rance
 */
public class SummonCommand implements Command {
    @Override
    public boolean onCommand(EntityPlayer player, String command, String[] args) {
        if (args.length == 0) return false;
        String type = args[0];
        World world;
        if ((world = player.getLocation().getWorld()) == null) return false;
        switch (type.toLowerCase()) {
            case "tnt":
                world.spawnEntity(EntityPrimedTNT.class, player.getLocation());
                break;
            case "zombie":
                world.spawnMonster(Entity.Type.ZOMBIE, player.getLocation());
                break;
            case "skeleton":
                world.spawnMonster(Entity.Type.SKELETON, player.getLocation());
                break;
            case "creeper":
                world.spawnMonster(Entity.Type.CREEPER, player.getLocation());
                break;
            case "drop":
                if (args.length > 1) {
                    String name = args[1];
                    Material material = Arrays.stream(Material.values()).filter(m -> m.getName().toLowerCase().replace(" ", "_").equals(name)).findFirst().orElse(null);
                    if (material == null || material.getTileType() == Tile.TileType.BACKGROUND) return false;
                    world.dropItem(new ItemStack(material), player.getLocation());
                    return true;
                }
                return false;
            default: return false;
        }
        return true;
    }
}
