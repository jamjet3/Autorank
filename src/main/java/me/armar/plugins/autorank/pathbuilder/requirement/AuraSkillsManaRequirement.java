package me.armar.plugins.autorank.pathbuilder.requirement;

import me.armar.plugins.autorank.language.Lang;
import me.armar.plugins.utils.pluginlibrary.Library;
import me.armar.plugins.utils.pluginlibrary.hooks.AuraSkillsHook;
import org.bukkit.entity.Player;

public class AuraSkillsManaRequirement extends AbstractRequirement {
    private AuraSkillsHook handler = null;
    private double requiredMana = -1.0F;

    public AuraSkillsManaRequirement() {
    }

    public String getDescription() {
        return Lang.AURA_SKILLS_MANA_REQUIREMENT.getConfigValue(this.requiredMana);
    }

    public String getProgressString(Player player) {
        double var10000 = this.handler.getMana(player);
        return var10000 + "/" + this.requiredMana;
    }

    protected boolean meetsRequirement(Player player) {
        if (!this.handler.isHooked()) {
            return false;
        } else {
            return this.handler.getMana(player) >= this.requiredMana;
        }
    }

    public boolean initRequirement(String[] options) {
        this.addDependency(Library.AURA_SKILLS);
        this.handler = (AuraSkillsHook)this.getDependencyManager().getLibraryHook(Library.AURA_SKILLS).orElse(null);
        if (options.length > 0) {
            try {
                this.requiredMana = Double.parseDouble(options[0]);
            } catch (NumberFormatException var3) {
                this.registerWarningMessage("An invalid number is provided");
                return false;
            }
        }

        if (this.requiredMana < (double)0.0F) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        } else {
            return this.handler != null;
        }
    }

    public double getProgressPercentage(Player player) {
        return this.handler.getMana(player) / this.requiredMana;
    }
}
