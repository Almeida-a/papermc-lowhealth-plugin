package pt.muc.mc.paperlowhealthplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;

public final class PaperLowhealthPlugin extends JavaPlugin implements Listener {

    private static final Logger logger = (Logger) Bukkit.getLogger();
    private static final int DEFAULT_PERCENTAGE_THRESHOLD = 50;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent damageEvent) {

        UUID entityId = damageEvent.getEntity().getUniqueId();

        // Get player
        Player player = Bukkit.getPlayer(entityId);
        if (player == null)
            return;

        // Get max-health value
        Optional<AttributeInstance> maxHealthAttr = Optional.ofNullable(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
        if (!maxHealthAttr.isPresent()) {
            logger.error("Could not fetch max health attribute!");
            return;
        }
        double maxHealth = maxHealthAttr.get().getValue();
        logger.debug("Player {} has taken damage: {} out of {}", player.getName(), player.getHealth(), maxHealth);

        int playerHealthPercentage = (int) (100 * player.getHealth() / maxHealth);

        if (playerHealthPercentage < DEFAULT_PERCENTAGE_THRESHOLD) {
            String message = String.format("Player %s has low health (%d%%)", player.getName(), playerHealthPercentage);
            Bukkit.broadcast(Component.text(message));
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        Bukkit.getPluginManager().registerEvents(this, this);
        logger.info(" \"Low-Health\" plugin has started!");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
