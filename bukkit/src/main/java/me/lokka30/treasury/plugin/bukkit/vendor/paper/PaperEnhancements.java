package me.lokka30.treasury.plugin.bukkit.vendor.paper;

import java.util.Arrays;
import me.lokka30.treasury.plugin.bukkit.Treasury;

public class PaperEnhancements {

    public static void enhance(Treasury plugin) {
        String pckg = plugin.getServer().getClass().getPackage().getName();
        int[] version = Arrays.stream(
                pckg.substring(pckg.lastIndexOf('.') + 1)
                        .replace("v", "")
                        .replace("_", ".")
                        .replace("R", "")
                        .split("\\.")
        ).mapToInt(Integer::parseInt).toArray();

        if (version[1] >= 15) {
            // brigadier enhancement
            plugin.getServer().getPluginManager().registerEvents(new PaperBrigadierEnhancement(), plugin);
        } else {
            // 1.12, 1.13 and 1.14
            if (version[1] >= 12) {
                // async completions
                plugin.getServer().getPluginManager().registerEvents(new PaperAsyncTabEnhancement(), plugin);
            }
        }
    }
}
