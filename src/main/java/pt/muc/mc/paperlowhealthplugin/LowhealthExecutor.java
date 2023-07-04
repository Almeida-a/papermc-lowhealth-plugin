package pt.muc.mc.paperlowhealthplugin;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pt.muc.mc.paperlowhealthplugin.commands.CommandFactory;
import pt.muc.mc.paperlowhealthplugin.commands.list.CommandParser;

public class LowhealthExecutor implements CommandExecutor {

    private final CommandFactory factory = new CommandFactory();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!isCommandValid(args))
            return false;

        CommandParser parser = factory.getCommand(args[0])
                .orElseThrow(() -> new RuntimeException("Internal error! " +
                        "Command was assured to be valid, but factory did not find it so."));

        String argJoined = StringUtils.join(args, " ");
        parser.execute(argJoined);

        return true;
    }

    /**
     * Ensures the command arguments are valid
     * @param args Command argument list
     * @return true if format of the arguments are correct
     */
    public boolean isCommandValid(String[] args) {

        String joined = StringUtils.join(args, " ");

        var parser = factory.getCommand(ArrayUtils.get(args, 0));

        return parser.map(validParser -> validParser.isValid(joined)).orElse(false);
    }
}
