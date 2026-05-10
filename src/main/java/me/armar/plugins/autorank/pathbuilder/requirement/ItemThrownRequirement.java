package me.armar.plugins.autorank.pathbuilder.requirement;

import java.util.UUID;
import me.armar.plugins.autorank.language.Lang;
import org.bukkit.Material;

public class ItemThrownRequirement extends AbstractRequirement {
    int numberofThrows = -1;
    Material itemThrown = null;

    public ItemThrownRequirement() {
    }

    public String getDescription() {
        String lang = Lang.ITEM_THROWN_REQUIREMENT.getConfigValue(this.numberofThrows, this.itemThrown);
        if (this.isWorldSpecific()) {
            lang = lang.concat(" (in world '" + this.getWorld() + "')");
        }

        return lang;
    }

    public String getProgressString(UUID uuid) {
        int var10000 = this.getStatisticsManager().getItemThrown(uuid, this.itemThrown);
        return var10000 + "/" + this.numberofThrows;
    }

    protected boolean meetsRequirement(UUID uuid) {
        return this.getStatisticsManager().getItemThrown(uuid, this.itemThrown) >= this.numberofThrows;
    }

    public boolean initRequirement(String[] options) {
        try {
            this.itemThrown = Material.getMaterial(options[0].trim().toUpperCase());
            this.numberofThrows = Integer.parseInt(options[1]);
        } catch (Exception var3) {
            this.registerWarningMessage("An invalid number is provided");
            return false;
        }

        if (this.numberofThrows < 0) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        } else {
            return this.numberofThrows > 0 && this.itemThrown != null;
        }
    }

    public double getProgressPercentage(UUID uuid) {
        return (double)this.getStatisticsManager().getItemThrown(uuid, this.itemThrown) / (double)this.numberofThrows;
    }
}
