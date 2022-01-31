package xyz.destiall.pixelate.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.states.StateGame;

/**
 * Written by Rance
 */
public class CommandMap {
    private final Map<String, Command> mappings;

    public CommandMap() {
        mappings = new HashMap<>();
    }

    /**
     * Execute a command
     * @param command The command to execute
     * @return true if it executed successfully, otherwise false
     */
    public boolean executeCommand(String command) {
        String[] args = command.toLowerCase().split(" ");
        String cmd = args.length == 0 ? command.toLowerCase() : args[0];
        Command executor = mappings.get(cmd);
        if (executor != null) {
            return executor.onCommand(((StateGame) Pixelate.getGSM().getState("Game")).getPlayer(), cmd, Arrays.copyOfRange(args, 1, args.length));
        }
        return false;
    }

    /**
     * Register a command with it's executor
     * @param command The string command to register
     * @param executor The command to execute
     */
    public void registerCommand(String command, Command executor) {
        if (command.contains(" ")) return;
        mappings.put(command.toLowerCase(), executor);
    }
}
