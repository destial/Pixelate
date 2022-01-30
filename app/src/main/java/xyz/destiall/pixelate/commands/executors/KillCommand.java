package xyz.destiall.pixelate.commands.executors;

import xyz.destiall.pixelate.commands.Command;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.status.Gamemode;

/**
 * Written by Rance
 */
public class KillCommand implements Command {
    @Override
    public boolean onCommand(EntityPlayer player, String command, String[] args) {
        if (args.length == 0) {
            player.damage(20);
            return true;
        }
        return false;
    }
}
