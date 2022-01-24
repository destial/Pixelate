package xyz.destiall.pixelate.commands;

import xyz.destiall.pixelate.entities.EntityPlayer;

/**
 * Written by Rance
 */
public interface Command {

    /**
     * This function gets called when the registered command is triggered
     * @param player The passed {@link EntityPlayer}
     * @param command The passed command
     * @param args The providing arguments, if any
     * @return true if successful, otherwise false
     */
    boolean onCommand(EntityPlayer player, String command, String[] args);
}
