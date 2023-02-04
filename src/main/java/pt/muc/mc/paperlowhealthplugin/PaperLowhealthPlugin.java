package pt.muc.mc.paperlowhealthplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Almeida-a
 */
public final class PaperLowhealthPlugin extends JavaPlugin implements Listener {

    private static final Logger logger = (Logger) Bukkit.getLogger();
    private final FileConfiguration configuration = this.getConfig();

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

        addAttributes();
        configuration.options().copyDefaults(true);
        this.saveConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        logger.info("\"Low-Health\" plugin has started!");

    }

    /**
     * Applies FileConfiguration$addDefault to the CustomConfiguration enums
     * <p>
     * This method should be used if the configurations are to be retrieved from
     *  the configuration (FileConfiguration) attribute.
     * For that reason, this method probably will not be used.
     */
    private void addAttributes() {

        Arrays.stream(CustomConfiguration.values()).toList().forEach(attribute -> {
            String attributeKey = attribute.name().toLowerCase();
            var attributeValue = attribute.getType().cast(attribute.getValue());
            configuration.addDefault(attributeKey, attributeValue);
        });

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
