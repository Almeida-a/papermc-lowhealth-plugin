package pt.muc.mc.paperlowhealthplugin;

import net.kyori.adventure.text.Component;
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

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Almeida-a
 */
public final class PaperLowhealthPlugin extends JavaPlugin implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(PaperLowhealthPlugin.class);
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
            logger.error("Could not fetch max health attribute!");
            return;
        }
        double maxHealth = maxHealthAttr.get().getValue();
        logger.debug("Player {} has taken damage: {} out of {}", player.getName(), player.getHealth(), maxHealth);

        Integer playerHealthPercentage = (int) (100 * player.getHealth() / maxHealth);

        Integer configuredThreshold = (Integer) CustomConfiguration.THRESHOLD_PERCENTAGE.getValue();
        if (playerHealthPercentage < configuredThreshold) {
            String message = String.format("Player %s has low health (%d%%)", player.getName(), playerHealthPercentage);
            Bukkit.broadcast(Component.text(message));
        }
    }

    @Override
    public void onEnable() {

        createCustomConfig();
        CustomConfiguration.loadConfiguration(customConfig);

        Bukkit.getPluginManager().registerEvents(this, this);
        logger.info("\"Low-Health\" plugin has started!");

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
                logger.info("Non-existent config.yml file. Creating new one");
            }
            saveResource(configFileBasename, false);
        }

        customConfig = new YamlConfiguration();

        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            logger.error("You have found a bug!");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
