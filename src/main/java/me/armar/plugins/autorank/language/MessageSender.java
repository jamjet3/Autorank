package me.armar.plugins.autorank.language;

import me.armar.plugins.autorank.Autorank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageSender {
    public MessageSender(Autorank plugin) {
    }

    public MessageSender(Player player, String message) {
        Bukkit.getLogger().info("MessageSender " + message);
    }

    public void SendMessage(Player player, String message) {
        Bukkit.getLogger().info("SendMessage " + message);
    }
}
