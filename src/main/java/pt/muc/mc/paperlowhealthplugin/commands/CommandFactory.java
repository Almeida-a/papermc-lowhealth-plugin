package pt.muc.mc.paperlowhealthplugin.commands;

import pt.muc.mc.paperlowhealthplugin.commands.list.CommandParser;
import pt.muc.mc.paperlowhealthplugin.commands.list.ThresholdParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandFactory {
    private final Map<String, CommandParser> commandMap;

    /**
     * Register here the new commands
     */
    public CommandFactory() {
        commandMap = new HashMap<>();
        commandMap.put("threshold", new ThresholdParser());
        // add more commands as needed
    }

    public Optional<CommandParser> getCommand(String commandName) {
        return Optional.ofNullable(commandMap.get(commandName));
    }
}
