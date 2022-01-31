package xyz.destiall.pixelate.commands.executors;

import java.lang.reflect.Field;

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
        try {
            Field field = Settings.class.getField(args[0].toUpperCase());
            field.set(null, !field.getBoolean(null));
            return true;
        } catch (Exception ignored) {}
        return false;
    }
}
