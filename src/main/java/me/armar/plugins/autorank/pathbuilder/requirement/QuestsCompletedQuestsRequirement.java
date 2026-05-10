package me.armar.plugins.autorank.pathbuilder.requirement;

import java.util.UUID;
import me.armar.plugins.autorank.hooks.quests.QuestsPlugin;
import me.armar.plugins.autorank.language.Lang;
import me.armar.plugins.utils.pluginlibrary.Library;

public class QuestsCompletedQuestsRequirement extends AbstractRequirement {
    private QuestsPlugin handler = null;
    private int completedQuests = -1;

    public QuestsCompletedQuestsRequirement() {
    }

    public String getDescription() {
        return Lang.QUESTS_COMPLETED_QUESTS_REQUIREMENT.getConfigValue(this.completedQuests);
    }

    public String getProgressString(UUID uuid) {
        int var10000 = this.handler.getNumberOfCompletedQuests(uuid);
        return var10000 + "/" + this.completedQuests;
    }

    protected boolean meetsRequirement(UUID uuid) {
        return this.handler.getNumberOfCompletedQuests(uuid) >= this.completedQuests;
    }

    public boolean initRequirement(String[] options) {
        this.addDependency(Library.QUESTS);
        this.addDependency(Library.QUESTS_ALTERNATIVE);
        this.handler = this.getDependencyManager().getQuestsPlugin().orElse(null);
        if (options.length > 0) {
            try {
                this.completedQuests = Integer.parseInt(options[0]);
            } catch (NumberFormatException var3) {
                this.registerWarningMessage("An invalid number is provided");
                return false;
            }
        }

        if (this.completedQuests < 0) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        } else {
            return this.handler != null;
        }
    }

    public double getProgressPercentage(UUID uuid) {
        return (double)this.handler.getNumberOfCompletedQuests(uuid) / (double)this.completedQuests;
    }
}
