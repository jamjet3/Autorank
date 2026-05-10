package me.armar.plugins.autorank.placeholders;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import me.armar.plugins.autorank.Autorank;
import me.armar.plugins.autorank.pathbuilder.Path;
import me.armar.plugins.autorank.storage.TimeType;
import me.armar.plugins.autorank.util.AutorankTools;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class AutorankPlaceholder extends PlaceholderExpansion {
    private final Autorank plugin;

    public AutorankPlaceholder(Autorank instance) {
        this.plugin = instance;
    }

    public boolean canRegister() {
        return true;
    }

    public @NotNull String getIdentifier() {
        return "autorank";
    }

    public @NotNull String getAuthor() {
        return "Ironic_8b49";
    }

    public @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("total_time_of_player")) {
            try {
                Object var43 = this.plugin.getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, player.getUniqueId()).get();
                return "" + var43;
            } catch (InterruptedException | ExecutionException var11) {
                return "Couldn't obtain total time of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("total_hours_of_player")) {
            try {
                Object var42 = this.plugin.getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, player.getUniqueId()).get();
                int totaltemp = Integer.parseInt("" + var42);
                double totalhour = (double)totaltemp / (double)60.0F;
                return Integer.toString((int)totalhour);
            } catch (InterruptedException | ExecutionException var12) {
                return "Couldn't obtain total time in hours of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("total_just_mins_of_player")) {
            try {
                Object var41 = this.plugin.getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, player.getUniqueId()).get();
                int totaltemp = Integer.parseInt("" + var41);
                int totalday = totaltemp / 1440;
                double dechour = totaltemp - totalday * 1440;
                double totalhour = dechour / (double)60.0F;
                double totalmin = dechour - (double)((int)totalhour * 60);
                return Integer.toString((int)totalmin);
            } catch (InterruptedException | ExecutionException var13) {
                return "Couldn't obtain total time in minutes of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("total_just_hours_of_player")) {
            try {
                Object var40 = this.plugin.getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, player.getUniqueId()).get();
                int totaltemp = Integer.parseInt("" + var40);
                int totalday = totaltemp / 1440;
                double totalhour = totaltemp - totalday * 1440;
                totalhour /= 60.0F;
                return Integer.toString((int)totalhour);
            } catch (InterruptedException | ExecutionException var14) {
                return "Couldn't obtain total time in hours of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("total_just_days_of_player")) {
            try {
                Object var39 = this.plugin.getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, player.getUniqueId()).get();
                int totaltemp = Integer.parseInt("" + var39);
                Integer totalday = totaltemp / 1440;
                return String.valueOf(totalday);
            } catch (InterruptedException | ExecutionException var151) {
                return "Couldn't obtain total time in days of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("total_time_of_player_formatted")) {
            try {
                return AutorankTools.timeToString(Math.toIntExact(this.plugin.getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, player.getUniqueId(), TimeUnit.MINUTES).get()), TimeUnit.MINUTES);
            } catch (InterruptedException | ExecutionException var16) {
                return "Couldn't obtain total time (formatted) of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("daily_time_of_player")) {
            try {
                Object var38 = this.plugin.getPlayTimeManager().getPlayTime(TimeType.DAILY_TIME, player.getUniqueId()).get();
                return "" + var38;
            } catch (InterruptedException | ExecutionException var17) {
                return "Couldn't obtain daily time of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("daily_time_of_player_formatted")) {
            try {
                return AutorankTools.timeToString(Math.toIntExact(this.plugin.getPlayTimeManager().getPlayTime(TimeType.DAILY_TIME, player.getUniqueId(), TimeUnit.MINUTES).get()), TimeUnit.MINUTES);
            } catch (InterruptedException | ExecutionException var18) {
                return "Couldn't obtain daily time (formatted) of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("weekly_time_of_player")) {
            try {
                Object var37 = this.plugin.getPlayTimeManager().getPlayTime(TimeType.WEEKLY_TIME, player.getUniqueId()).get();
                return "" + var37;
            } catch (InterruptedException | ExecutionException var19) {
                return "Couldn't obtain weekly time of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("weekly_time_of_player_formatted")) {
            try {
                return AutorankTools.timeToString(Math.toIntExact(this.plugin.getPlayTimeManager().getPlayTime(TimeType.WEEKLY_TIME, player.getUniqueId(), TimeUnit.MINUTES).get()), TimeUnit.MINUTES);
            } catch (InterruptedException | ExecutionException var20) {
                return "Couldn't obtain weekly time (formatted) of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("monthly_time_of_player")) {
            try {
                Object var36 = this.plugin.getPlayTimeManager().getPlayTime(TimeType.MONTHLY_TIME, player.getUniqueId()).get();
                return "" + var36;
            } catch (InterruptedException | ExecutionException var21) {
                return "Couldn't obtain monthly time of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("monthly_time_of_player_formatted")) {
            try {
                return AutorankTools.timeToString(Math.toIntExact(this.plugin.getPlayTimeManager().getPlayTime(TimeType.MONTHLY_TIME, player.getUniqueId(), TimeUnit.MINUTES).get()), TimeUnit.MINUTES);
            } catch (InterruptedException | ExecutionException var22) {
                return "Couldn't obtain monthly time (formatted) of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("local_time")) {
            try {
                Object var35 = this.plugin.getPlayTimeManager().getLocalPlayTime(TimeType.TOTAL_TIME, player.getUniqueId()).get();
                return "" + var35;
            } catch (InterruptedException | ExecutionException var23) {
                return "Couldn't obtain local time of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("local_time_formatted")) {
            try {
                return AutorankTools.timeToString(Math.toIntExact((long) this.plugin.getPlayTimeManager().getLocalPlayTime(TimeType.TOTAL_TIME, player.getUniqueId()).get()), TimeUnit.MINUTES);
            } catch (InterruptedException | ExecutionException var24) {
                return "Couldn't obtain local time (formatted) of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("global_time")) {
            try {
                Object var10000 = this.plugin.getPlayTimeManager().getGlobalPlayTime(TimeType.TOTAL_TIME, player.getUniqueId()).get();
                return "" + var10000;
            } catch (InterruptedException | ExecutionException var25) {
                return "Couldn't obtain global time of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("global_time_formatted")) {
            try {
                return AutorankTools.timeToString(Math.toIntExact((long) this.plugin.getPlayTimeManager().getGlobalPlayTime(TimeType.TOTAL_TIME, player.getUniqueId()).get()), TimeUnit.MINUTES);
            } catch (InterruptedException | ExecutionException var26) {
                return "Couldn't obtain global time (formatted) of " + player.getName();
            }
        } else if (params.equalsIgnoreCase("completed_paths")) {
            return this.plugin.getAPI().getCompletedPaths(player.getUniqueId()).stream().map(Path::getDisplayName).collect(Collectors.joining(","));
        } else if (params.equalsIgnoreCase("active_paths")) {
            return this.plugin.getAPI().getActivePaths(player.getUniqueId()).stream().map(Path::getDisplayName).collect(Collectors.joining(","));
        } else {
            return params.equalsIgnoreCase("eligible_paths") ? this.plugin.getAPI().getEligiblePaths(player.getUniqueId()).stream().map(Path::getDisplayName).collect(Collectors.joining(",")) : null;
        }
    }

    public boolean persist() {
        return true;
    }
}
