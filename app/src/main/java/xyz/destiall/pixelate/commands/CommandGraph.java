package xyz.destiall.pixelate.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.states.StateGame;

/**
 * Written by Rance
 */
public class CommandGraph {
    private final Map<String, Command> mappings;

    public CommandGraph() {
        mappings = new HashMap<>();
    }

    public boolean executeCommand(String command) {
        String[] args = command.toLowerCase().split(" ");
        String cmd = args.length == 0 ? command.toLowerCase() : args[0];
        Command executor = mappings.get(cmd);
        if (executor != null) {
            return executor.onCommand(((StateGame) Pixelate.getGSM().getState("Game")).getPlayer(), cmd, Arrays.copyOfRange(args, 1, args.length));
        }
        return false;
    }

    public void registerCommand(String command, Command executor) {
        if (command.contains(" ")) return;
        mappings.put(command.toLowerCase(), executor);
    }
}
