package pt.muc.mc.paperlowhealthplugin;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.muc.mc.paperlowhealthplugin.configparameters.ConfigurationParameters;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Almeida-a
 */
public final class PaperLowhealthPlugin extends JavaPlugin implements Listener {

    private static final Logger myLogger = LoggerFactory.getLogger(PaperLowhealthPlugin.class);
    private FileConfiguration customConfig;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent damageEvent) {

        UUID entityId = damageEvent.getEntity().getUniqueId();

        // Get player
        Player player = Bukkit.getPlayer(entityId);
        if (player == null)
            return;

        // Get max-health value
        Optional<AttributeInstance> maxHealthAttr = Optional.ofNullable(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
        if (maxHealthAttr.isEmpty()) {
            myLogger.error("Could not fetch max health attribute!");
            return;
        }
        double maxHealth = maxHealthAttr.get().getValue();
        myLogger.debug("Player {} has taken damage: {} out of {}", player.getName(), player.getHealth(), maxHealth);

        int playerHealthPercentage = (int) (100 * player.getHealth() / maxHealth);

        int configuredThreshold = NumberUtils.toInt(ConfigurationParameters.THRESHOLD_PERCENTAGE.getValue());
        if (playerHealthPercentage < configuredThreshold) {
            Broadcaster.send("Player %s has low health (%d%%)", player.getName(), playerHealthPercentage);
        }
    }

    @Override
    public void onEnable() {

        // Extract configurations from config.yml
        createCustomConfig();
        ConfigurationParameters.loadConfiguration(customConfig, myLogger);

        // Register commands
        Objects.requireNonNull(this.getCommand("lowhealth")).setExecutor(new LowhealthExecutor());

        Bukkit.getPluginManager().registerEvents(this, this);
        myLogger.info("\"Low-Health\" plugin has started!");

    }

    /**
     * Loads the configurations from config.yml
     * <p>
     * As in <a href="https://www.spigotmc.org/wiki/config-files/#using-custom-configurations">this</a> guide
     */
    private void createCustomConfig() {

        String configFileBasename = "config.yml";

        File customConfigFile = new File(getDataFolder(), configFileBasename);
        if (!customConfigFile.exists()) {
            if (customConfigFile.getParentFile().mkdirs()) {
                myLogger.info("Non-existent config.yml file. Creating new one");
            }
            saveResource(configFileBasename, false);
        }

        customConfig = new YamlConfiguration();

        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            myLogger.error("You have found a bug!");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
