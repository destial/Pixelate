package xyz.destiall.pixelate.commands;

import xyz.destiall.pixelate.entities.EntityPlayer;

/**
 * Written by Rance
 */
public interface Command {
    boolean onCommand(EntityPlayer player, String command, String[] args);
}
