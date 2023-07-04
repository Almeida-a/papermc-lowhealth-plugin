package pt.muc.mc.paperlowhealthplugin.configparameters;

import org.apache.commons.text.CaseUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.slf4j.Logger;
import pt.muc.mc.paperlowhealthplugin.configparameters.list.ThresholdValueValidator;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This class holds the (global) configuration attributes of the plugin
 *
 * @author Almeida-a
 */
public enum ConfigurationParameters {

    THRESHOLD_PERCENTAGE("50", new ThresholdValueValidator());

    private String value;
    private final Predicate<String> validator;

    ConfigurationParameters(String defaultValue, Predicate<String> validator) {
        value = defaultValue;
        this.validator = validator;
    }

    /**
     * @return a camelCase formatted name
     */
    public String nameToLowerCamelCase() {
        return CaseUtils.toCamelCase(name(), false, '_');
    }

    public static void loadConfiguration(FileConfiguration configuration, Logger logger) {
        Arrays.stream(ConfigurationParameters.values()).toList().forEach(attribute -> {
            String attributeKey = attribute.nameToLowerCamelCase();
            attribute.value = Optional.ofNullable(configuration.getString(attributeKey)).orElseGet(() -> {
                logger.info("Attribute '{}' not found at 'config.yml'. Setting as default '{}'", attributeKey, attribute.value);
                return attribute.value;
            });
        });
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Predicate<String> getValidator() {
        return validator;
    }

}
