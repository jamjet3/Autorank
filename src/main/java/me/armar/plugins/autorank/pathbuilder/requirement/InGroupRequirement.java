package me.armar.plugins.autorank.pathbuilder.requirement;

import me.armar.plugins.autorank.language.Lang;
import org.bukkit.entity.Player;

public class InGroupRequirement extends AbstractRequirement {
    String group = null;

    public InGroupRequirement() {
    }

    public String getDescription() {
        return Lang.GROUP_REQUIREMENT.getConfigValue(this.group);
    }

    public String getProgressString(Player player) {
        for(String groupString : this.getAutorank().getPermPlugHandler().getPermissionPlugin().getPlayerGroups(player)) {
            if (groupString.equalsIgnoreCase(this.group)) {
                return "you're in the group!";
            }
        }

        return "you're not in the group";
    }

    public boolean meetsRequirement(Player player) {
        for(String groupString : this.getAutorank().getPermPlugHandler().getPermissionPlugin().getPlayerGroups(player)) {
            if (groupString.equalsIgnoreCase(this.group)) {
                return true;
            }
        }

        return false;
    }

    public boolean initRequirement(String[] options) {
        if (options.length > 0) {
            this.group = options[0].trim();
        }

        if (this.group == null) {
            this.registerWarningMessage("No group is provided");
            return false;
        } else {
            return true;
        }
    }

    public boolean needsOnlinePlayer() {
        return true;
    }
}
