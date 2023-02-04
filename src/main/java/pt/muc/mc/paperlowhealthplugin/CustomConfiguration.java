package pt.muc.mc.paperlowhealthplugin;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

/**
 * This class holds the configuration attributes of the plugin,
 *  that will be present in the config.yml
 *  - Paths
 *  - Values
 *
 * @author Almeida-a
 */
public enum CustomConfiguration {
    THRESHOLD_PERCENTAGE(50, Integer.class);

    private Object value;
    private Class<?> type;

    CustomConfiguration(Object defaultValue, Class<?> type) {
        value = defaultValue;
        this.type = type;
    }

    public void loadConfiguration(FileConfiguration configuration) {
        Arrays.stream(CustomConfiguration.values()).toList().forEach(attribute -> {
            String attributeKey = attribute.name().toLowerCase();
            attribute.value = configuration.getObject(attributeKey, attribute.getType());
        });
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
