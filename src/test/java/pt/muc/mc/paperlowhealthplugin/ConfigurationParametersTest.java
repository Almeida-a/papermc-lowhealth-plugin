package pt.muc.mc.paperlowhealthplugin;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.CaseUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.LoggerFactory;
import pt.muc.mc.paperlowhealthplugin.configparameters.ConfigurationParameters;
import pt.muc.mc.paperlowhealthplugin.configparameters.list.ThresholdValueValidator;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationParametersTest {

    @org.junit.jupiter.api.Test
    void nameToLowerCamelCase() {
        assertEquals(
                "thisStringIntoLowerCamelCase",
                CaseUtils.toCamelCase("THIS_STRING_INTO_LOWER_CAMEL_CASE", false, '_')
        );
    }

    @org.junit.jupiter.api.Test
    void loadConfiguration() {
        ThresholdValueValidator threshValidator = new ThresholdValueValidator();

        var templateConfig = new YamlConfiguration();

        assertDoesNotThrow(() -> templateConfig.load("src/main/resources/config.yml"));

        // Read value at config.yml
        String configThresh = templateConfig.getString("thresholdPercentage");
        assertNotNull(configThresh);
        int configThreshVal = NumberUtils.toInt(configThresh, -1);
        assertTrue(threshValidator.test(configThresh));
        assertNotEquals(-1, configThreshVal);

        // Set threshold to a different value
        String hardcodedThresh = ConfigurationParameters.THRESHOLD_PERCENTAGE.getValue();
        assertTrue(threshValidator.test(hardcodedThresh));
        int hardcodedThreshVal = NumberUtils.toInt(hardcodedThresh, -1);
        assertNotEquals(-1, hardcodedThreshVal);
        if (configThreshVal == hardcodedThreshVal) {
            hardcodedThreshVal = (hardcodedThreshVal + 1) % 100;
            ConfigurationParameters.THRESHOLD_PERCENTAGE.setValue(String.valueOf(hardcodedThreshVal));
        }
        assertNotEquals(hardcodedThreshVal, configThreshVal);

        // Call loadConfiguration and check that threshold is of the same value as the one read on config.yml
        ConfigurationParameters.loadConfiguration(templateConfig, LoggerFactory.getLogger("test"));
        hardcodedThreshVal = NumberUtils.toInt(ConfigurationParameters.THRESHOLD_PERCENTAGE.getValue(), -1);
        assertEquals(hardcodedThreshVal, configThreshVal);

    }
}