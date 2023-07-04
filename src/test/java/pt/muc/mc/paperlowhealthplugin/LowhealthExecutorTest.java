package pt.muc.mc.paperlowhealthplugin;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.muc.mc.paperlowhealthplugin.commands.CommandFactory;
import pt.muc.mc.paperlowhealthplugin.commands.list.CommandParser;
import pt.muc.mc.paperlowhealthplugin.configparameters.ConfigurationParameters;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class LowhealthExecutorTest {

    @Test
    void onThresholdCommand() {

        HashMap<String, String[][]> validCommands = new HashMap<>();
        validCommands.put("threshold", new String[][]{
                {"set", "30"},
                {"set", "100"},
                {"set", "0"},
                {"get"},
        });

        HashMap<String, String[][]> invalidCommands = new HashMap<>();
        invalidCommands.put("threshold", new String[][]{
                {},
                {"set", "NaN"},
                {"set", "12", "another parameter"},
                {"set", "-1"},
                {"set", "121"},
                {"set"},
                {"get", "another argument"},
                {"unknown"},
                {"unknown", "unknown argument"},
        });

        var testCommandKit = new LowhealthExecutor();

        // Test out valid commands
        for (String commandName : validCommands.keySet()) {
            for (String[] argumentsList : validCommands.get(commandName)) {
                String[] fullCommandArgs = ArrayUtils.insert(0, argumentsList, "threshold");
                assertTrue(testCommandKit.isCommandValid(fullCommandArgs));
            }
        }

        // Test out invalid commands
        for (String commandName : invalidCommands.keySet()) {
            for (String[] argumentsList : invalidCommands.get(commandName)) {
                String[] fullCommandArgs = ArrayUtils.insert(0, argumentsList, "threshold");
                assertFalse(testCommandKit.isCommandValid(fullCommandArgs));
            }
        }

        // Test if the threshold set command actually changes the config
        CommandParser thresholdParser = new CommandFactory().getCommand("threshold").or(Assertions::fail).get();
        thresholdParser.execute("threshold set 10");
        assertEquals("10", ConfigurationParameters.THRESHOLD_PERCENTAGE.getValue());
        thresholdParser.execute("threshold set 20");
        assertEquals("20", ConfigurationParameters.THRESHOLD_PERCENTAGE.getValue());

        // TODO: 21/05/23 Check (if possible) if the get command broadcasts the message as intended
    }
}