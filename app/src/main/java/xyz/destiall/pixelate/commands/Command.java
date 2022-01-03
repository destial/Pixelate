package xyz.destiall.pixelate.commands;

import xyz.destiall.pixelate.entities.EntityPlayer;

public interface Command {
    boolean onCommand(EntityPlayer player, String command, String[] args);
}
