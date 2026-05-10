package me.armar.plugins.autorank.pathbuilder.requirement;

import java.util.UUID;
import me.armar.plugins.autorank.language.Lang;
import me.armar.plugins.utils.pluginlibrary.Library;
import me.armar.plugins.utils.pluginlibrary.hooks.BentoBoxHook;
import me.armar.plugins.utils.pluginlibrary.hooks.LibraryHook;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class BentoBoxLevelRequirement extends AbstractRequirement {
    private String islandWorld = "askyblock";
    private int islandLevel = -1;
    private BentoBoxHook handler = null;

    public BentoBoxLevelRequirement() {
    }

    public String getDescription() {
        return Lang.BENTOBOX_LEVEL_REQUIREMENT.getConfigValue("" + this.islandLevel, this.islandWorld);
    }

    public String getProgressString(UUID uuid) {
        World world = Bukkit.getServer().getWorld(this.islandWorld);
        Long var10000 = this.handler.getIslandLevel(world, uuid);
        return var10000 + "/" + this.islandLevel;
    }

    public boolean meetsRequirement(UUID uuid) {
        World world = Bukkit.getServer().getWorld(this.islandWorld);
        return this.handler.getIslandLevel(world, uuid) >= (long)this.islandLevel;
    }

    public boolean initRequirement(String[] options) {
        this.addDependency(Library.BENTOBOX);
        this.handler = (BentoBoxHook)this.getAutorank().getDependencyManager().getLibraryHook(Library.BENTOBOX).orElse(null);

        try {
            this.islandLevel = Integer.parseInt(options[0]);
        } catch (NumberFormatException var3) {
            this.registerWarningMessage("An invalid number is provided");
            return false;
        }

        if (options.length > 1) {
            this.islandWorld = options[1];
        }

        if (this.islandLevel < 0) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        } else if (this.handler != null && this.handler.isHooked()) {
            return true;
        } else {
            this.registerWarningMessage("Island Level is not available");
            return false;
        }
    }

    public double getProgressPercentage(UUID uuid) {
        World world = Bukkit.getServer().getWorld(this.islandWorld);
        return (double)this.handler.getIslandLevel(world, uuid) / (double)this.islandLevel;
    }
}
