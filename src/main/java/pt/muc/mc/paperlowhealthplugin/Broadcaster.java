package pt.muc.mc.paperlowhealthplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

/**
 * Lowhealth methods for broadcasting messages in minecraft server
 */
public class Broadcaster {

    private Broadcaster() {}

    public static int send(String message) {
        return Bukkit.broadcast(Component.text(message));
    }

    public static int send(String message, Object... args) {
        return Bukkit.broadcast(Component.text(String.format(message, args)));
    }

}
