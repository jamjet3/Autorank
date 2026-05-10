package me.armar.plugins.autorank.pathbuilder.requirement;

import java.util.UUID;
import me.armar.plugins.autorank.language.Lang;

public class ItemsEnchantedRequirement extends AbstractRequirement {
    int itemsEnchanted = -1;

    public ItemsEnchantedRequirement() {
    }

    public String getDescription() {
        String lang = Lang.ITEMS_ENCHANTED_REQUIREMENT.getConfigValue(this.itemsEnchanted);
        if (this.isWorldSpecific()) {
            lang = lang.concat(" (in world '" + this.getWorld() + "')");
        }

        return lang;
    }

    public String getProgressString(UUID uuid) {
        int var10000 = this.getStatisticsManager().getItemsEnchanted(uuid);
        return var10000 + "/" + this.itemsEnchanted;
    }

    protected boolean meetsRequirement(UUID uuid) {
        return this.getStatisticsManager().getItemsEnchanted(uuid) >= this.itemsEnchanted;
    }

    public boolean initRequirement(String[] options) {
        try {
            this.itemsEnchanted = Integer.parseInt(options[0]);
        } catch (Exception var3) {
            this.registerWarningMessage("An invalid number is provided");
            return false;
        }

        if (this.itemsEnchanted < 0) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        } else {
            return true;
        }
    }

    public double getProgressPercentage(UUID uuid) {
        return (double)this.getStatisticsManager().getItemsEnchanted(uuid) / (double)this.itemsEnchanted;
    }
}
