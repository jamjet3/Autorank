package me.armar.plugins.autorank.converter;

import java.io.File;

import me.armar.plugins.autorank.Autorank;

public class DataConverter {
    private final Autorank plugin;

    public DataConverter(Autorank instance) {
        this.plugin = instance;
    }

    public boolean convertData() {
        if (this.plugin.getInternalPropertiesConfig().isConvertedToNewFormat()) {
            return false;
        } else {
            this.plugin.getLogger().info("Autorank detected that you upgraded from an older version. It will need to convert your folders.");
            this.plugin.getLogger().info("Started converting folders of Autorank...");
            String var10000 = this.plugin.getDataFolder().getAbsolutePath();
            String folderPath = var10000 + File.separator;
            if (!(new File(folderPath + "/storage/daily_time.yml")).renameTo(new File(folderPath + "/storage/Daily_time.yml"))) {
                this.plugin.getLogger().info("Could not rename daily_time.yml to Daily_time.yml!");
            } else {
                this.plugin.getLogger().info("Successfully converted Daily_time.yml!");
            }

            if (!(new File(folderPath + "/storage/weekly_time.yml")).renameTo(new File(folderPath + "/storage/Weekly_time.yml"))) {
                this.plugin.getLogger().info("Could not rename weekly_time.yml to Weekly_time.yml!");
            } else {
                this.plugin.getLogger().info("Successfully converted Weekly_time.yml!");
            }

            if (!(new File(folderPath + "/storage/monthly_time.yml")).renameTo(new File(folderPath + "/storage/Monthly_time.yml"))) {
                this.plugin.getLogger().info("Could not rename monthly_time.yml to Monthly_time.yml!");
            } else {
                this.plugin.getLogger().info("Successfully converted Monthly_time.yml!");
            }

            File totalTimeFile = new File(folderPath + "/storage/Total_time.yml");
            if (totalTimeFile.exists()) {
                this.plugin.getLogger().info("Deleting Total_time.yml");
                totalTimeFile.delete();
            }

            if (!(new File(folderPath + "Data.yml")).renameTo(totalTimeFile)) {
                this.plugin.getLogger().info("Could not rename Data.yml to Total_time.yml!");
            } else {
                this.plugin.getLogger().info("Successfully converted Data.yml!");
            }

            if (!(new File(folderPath + "/playerdata/playerdata.yml")).renameTo(new File(folderPath + "/playerdata/PlayerData.yml"))) {
                this.plugin.getLogger().info("Could not rename playerdata.yml to PlayerData.yml!");
            } else {
                this.plugin.getLogger().info("Successfully converted playerdata.yml!");
            }

            this.plugin.getLogger().info("Conversion of Autorank is complete!");
            this.plugin.getInternalPropertiesConfig().setConvertedToNewFormat(true);
            return true;
        }
    }

}
