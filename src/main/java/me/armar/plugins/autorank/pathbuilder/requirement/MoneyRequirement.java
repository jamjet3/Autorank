package me.armar.plugins.autorank.pathbuilder.requirement;

import me.armar.plugins.autorank.language.Lang;
import me.armar.plugins.utils.pluginlibrary.Library;
import me.armar.plugins.utils.pluginlibrary.hooks.VaultHook;
import org.bukkit.entity.Player;

public class MoneyRequirement extends AbstractRequirement {
    double minMoney = -1.0F;

    public MoneyRequirement() {
    }

    public String getDescription() {
        String currencyName = "";
        if (this.getAutorank().getDependencyManager().isAvailable(Library.VAULT)) {
            currencyName = VaultHook.getEconomy().currencyNamePlural().trim();
        }

        Lang var10000 = Lang.MONEY_REQUIREMENT;
        Object[] var10001 = new Object[1];
        long var10004 = Math.round(this.minMoney);
        var10001[0] = var10004 + " " + currencyName;
        String lang = var10000.getConfigValue(var10001);
        if (this.isWorldSpecific()) {
            lang = lang.concat(" (in world '" + this.getWorld() + "')");
        }

        return lang;
    }

    public String getProgressString(Player player) {
        double money = 0.0F;
        String currencyName = "";
        if (this.getAutorank().getDependencyManager().isAvailable(Library.VAULT)) {
            money = VaultHook.getEconomy().getBalance(player.getPlayer());
            currencyName = VaultHook.getEconomy().currencyNamePlural().trim();
        }

        return money + "/" + this.minMoney + " " + currencyName;
    }

    public boolean meetsRequirement(Player player) {
        if (this.isWorldSpecific() && !this.getWorld().equals(player.getWorld().getName())) {
            return false;
        } else {
            return this.getAutorank().getDependencyManager().isAvailable(Library.VAULT) && VaultHook.getEconomy() != null && VaultHook.getEconomy().has(player.getPlayer(), this.minMoney);
        }
    }

    public boolean initRequirement(String[] options) {
        this.addDependency(Library.VAULT);

        try {
            this.minMoney = Double.parseDouble(options[0]);
        } catch (Exception var3) {
            this.registerWarningMessage("An invalid number is provided");
            return false;
        }

        if (this.minMoney < (double)0.0F) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        } else {
            return true;
        }
    }

    public boolean needsOnlinePlayer() {
        return true;
    }
}
