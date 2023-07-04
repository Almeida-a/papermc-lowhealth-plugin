package pt.muc.mc.paperlowhealthplugin.commands.list;

/**
 * All the classes in this package must implement this interface
 */
public interface CommandParser {

    void execute(String command);
    boolean isValid(String command);

}
