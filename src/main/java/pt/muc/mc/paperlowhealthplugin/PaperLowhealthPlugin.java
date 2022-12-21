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
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class PaperLowhealthPlugin extends JavaPlugin implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(PaperLowhealthPlugin.class);

    @EventHandler
    public void onPlayerSomething(EntityDamageEvent damageEvent) {
        UUID entityId = damageEvent.getEntity().getUniqueId();

        // Get player
        Player player = Bukkit.getPlayer(entityId);
        if (player == null)
            return;

        // Get max-health value
        AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttr == null) {
            logger.error("Could not fetch max health attribute!");
            return;
        }
        double maxHealth = maxHealthAttr.getValue();

        int playerHealthPercentage = (int) (100 * player.getHealth() / maxHealth);

        if (playerHealthPercentage < 0.3) {
            String message = String.format("Player %s has low health (%d%%)", player.getName(), playerHealthPercentage);
            Bukkit.broadcast(Component.text(message));
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
