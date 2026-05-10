package me.armar.plugins.autorank.warningmanager;

import me.armar.plugins.autorank.Autorank;
import org.bukkit.entity.Player;

public class WarningNoticeTask implements Runnable {
    private final Autorank plugin;

    public WarningNoticeTask(Autorank instance) {
        this.plugin = instance;
    }

    public void run() {
        this.plugin.debugMessage("Run task to show warnings");
        this.plugin.getWarningManager().sendWarnings(this.plugin.getServer().getConsoleSender());
        if (this.plugin.getSettingsConfig().showWarnings()) {
            for(Player p : this.plugin.getServer().getOnlinePlayers()) {
                if (p.hasPermission("autorank.noticeonwarning") || p.isOp()) {
                    this.plugin.getWarningManager().sendWarnings(p);
                }
            }

        }
    }
}
