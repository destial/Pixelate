package xyz.destiall.pixelate.commands.executors;

import xyz.destiall.pixelate.commands.Command;
import xyz.destiall.pixelate.entities.EntityPlayer;

/**
 * Written by Rance
 */
public class XPCommand implements Command {
    @Override
    public boolean onCommand(EntityPlayer player, String command, String[] args) {
        if (args.length == 1) {
            try {
                int xp = Integer.parseInt(args[0]);
                player.addXP(xp);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
