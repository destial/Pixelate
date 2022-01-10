package xyz.destiall.pixelate.commands.executors;

import xyz.destiall.pixelate.commands.Command;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.settings.Settings;

/**
 * Written by Rance
 */
public class SettingsCommand implements Command {
    @Override
    public boolean onCommand(EntityPlayer player, String command, String[] args) {
        if (args.length == 0) return false;
        switch (args[0].toLowerCase()) {
            case "hitboxes": {
                Settings.ENABLE_HITBOXES = !Settings.ENABLE_HITBOXES;
                break;
            }
            case "crosshair": {
                Settings.ENABLE_CROSSHAIR = !Settings.ENABLE_CROSSHAIR;
                break;
            }
            case "blocktrace": {
                Settings.ENABLE_BLOCK_TRACE = !Settings.ENABLE_BLOCK_TRACE;
                break;
            }
            default: break;
        }
        return true;
    }
}
