package xyz.destiall.pixelate.commands.executors;

import xyz.destiall.pixelate.commands.Command;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.entities.Gamemode;

/**
 * Written by Rance
 */
public class GamemodeCommand implements Command {
    @Override
    public boolean onCommand(EntityPlayer player, String command, String[] args) {
        if (args.length == 0) return false;
        Gamemode gm = null;
        int i = 0;
        for (Gamemode gameMode : Gamemode.values()) {
            if (gameMode.name().equalsIgnoreCase(args[0]) || ("" + i).equals(args[0]) || gameMode.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                gm = gameMode;
                break;
            }
            i++;
        }
        if (gm == null) return false;

        player.setGamemode(gm);
        return true;
    }
}
