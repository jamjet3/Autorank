package me.armar.plugins.autorank.playtimes;

import java.util.UUID;
import me.armar.plugins.autorank.Autorank;
import me.armar.plugins.autorank.storage.TimeType;
import org.bukkit.entity.Player;

public class UpdateTimePlayedTask implements Runnable {
    private final Autorank plugin;
    private final UUID uuid;

    public UpdateTimePlayedTask(Autorank instance, UUID uuid) {
        this.plugin = instance;
        this.uuid = uuid;
    }

    public void run() {
        this.plugin.debugMessage("Run task to update play time of " + this.uuid);
        Player player = this.plugin.getServer().getPlayer(this.uuid);
        if (player != null && player.isOnline()) {
            this.plugin.getTaskManager().setLastPlayTimeUpdate(this.uuid, System.currentTimeMillis());
            this.plugin.debugMessage("Updating play time of " + player.getName());
            this.plugin.getPlayerChecker().doOfflineExemptionChecks(player);
            if (player.hasPermission("autorank.timeexclude")) {
                this.plugin.debugMessage("Player " + player.getName() + " is excluded from time updates by given permissions.");
            } else if (this.plugin.getDependencyManager().isAFK(player)) {
                this.plugin.debugMessage("Player " + player.getName() + " is AFK and so we don't add time.");
            } else {
                TimeType[] var2 = TimeType.values();

                for(TimeType type : var2) {
                    this.plugin.getPlayTimeStorageManager().addPlayerTime(type, this.uuid, PlayTimeManager.INTERVAL_MINUTES);
                }

                this.plugin.getPathManager().autoAssignPaths(player.getUniqueId());
                if (!this.plugin.getSettingsConfig().isAutomaticPathDisabled()) {
                    this.plugin.getPlayerChecker().checkPlayer(player.getUniqueId());
                }
            }
        } else {
            this.plugin.debugMessage("Cancelling update play time of " + this.uuid + " as he's not online.");
        }

    }
}
