package pt.muc.mc.paperlowhealthplugin.commands.list;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import pt.muc.mc.paperlowhealthplugin.Broadcaster;
import pt.muc.mc.paperlowhealthplugin.configparameters.ConfigurationParameters;

@Slf4j
public class ThresholdParser implements CommandParser {

    private static final String SET_REGEX = "threshold set \\d*";
    private static final String GET_REGEX = "threshold get";

    @Override
    public void execute(String command) {
        if (!isValid(command)) {
            log.error("Invalid command: '{}'!", command);
            return;
        }

        if (command.matches(GET_REGEX)) {
            int value = NumberUtils.toInt(ConfigurationParameters.THRESHOLD_PERCENTAGE.getValue());
            Broadcaster.send("Current threshold value is: '%s'", value);
        } else if (command.matches(SET_REGEX)) {
            String newValue = command.split(" ")[2];
            ConfigurationParameters.THRESHOLD_PERCENTAGE.setValue(newValue);
        }

    }

    @Override
    public boolean isValid(String command) {

        if (command.matches(SET_REGEX)) {
            String newValStr = ArrayUtils.get(command.split(" "), 2);
            var validator = ConfigurationParameters.THRESHOLD_PERCENTAGE.getValidator();
            return validator.test(newValStr);
        } else
            return command.matches(GET_REGEX);

    }
}
