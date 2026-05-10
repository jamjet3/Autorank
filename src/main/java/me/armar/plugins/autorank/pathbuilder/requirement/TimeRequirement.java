package me.armar.plugins.autorank.pathbuilder.requirement;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import me.armar.plugins.autorank.language.Lang;
import me.armar.plugins.autorank.storage.TimeType;
import me.armar.plugins.autorank.util.AutorankTools;

public class TimeRequirement extends AbstractRequirement {
    int timeNeeded = -1;

    public TimeRequirement() {
    }

    public String getDescription() {
        return Lang.TIME_REQUIREMENT.getConfigValue(AutorankTools.timeToString(this.timeNeeded, TimeUnit.MINUTES));
    }

    public String getProgressString(UUID uuid) {
        int playtime = 0;

        try {
            playtime = this.getAutorank().getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, uuid).get();
        } catch (InterruptedException | ExecutionException var4) {
            var4.printStackTrace();
        }

        String var10000 = AutorankTools.timeToString(playtime, TimeUnit.MINUTES);
        return var10000 + "/" + AutorankTools.timeToString(this.timeNeeded, TimeUnit.MINUTES);
    }

    protected boolean meetsRequirement(UUID uuid) {
        int playTime = 0;

        try {
            playTime = this.getAutorank().getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, uuid).get();
        } catch (InterruptedException | ExecutionException var4) {
            var4.printStackTrace();
        }

        return this.timeNeeded != -1 && playTime >= this.timeNeeded;
    }

    public boolean initRequirement(String[] options) {
        if (options.length > 0) {
            this.timeNeeded = AutorankTools.stringToTime(options[0], TimeUnit.MINUTES);
        }

        if (this.timeNeeded < 0) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        } else {
            return true;
        }
    }

    public double getProgressPercentage(UUID uuid) {
        int playtime = 0;

        try {
            playtime = this.getAutorank().getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, uuid).get();
        } catch (InterruptedException | ExecutionException var4) {
            var4.printStackTrace();
        }

        return (double)playtime / (double)this.timeNeeded;
    }
}
