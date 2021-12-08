/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.paper;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;

import java.util.Arrays;

public class PaperEnhancements {

    public static void enhance(TreasuryBukkit plugin) {
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
