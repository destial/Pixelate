package xyz.destiall.pixelate.commands.executors;

import xyz.destiall.pixelate.commands.Command;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.position.Location;

/**
 * Written by Rance
 */
public class SpawnCommand implements Command {
    @Override
    public boolean onCommand(EntityPlayer player, String command, String[] args) {
        player.teleport(player.getLocation().getWorld().getNearestEmpty(new Location(0, 0)));
        return true;
    }
}
