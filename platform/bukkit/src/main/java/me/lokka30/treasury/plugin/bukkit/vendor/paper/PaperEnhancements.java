/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.paper;

import java.util.Arrays;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;

public class PaperEnhancements {

    public static void enhance(TreasuryBukkit plugin) {
        String pckg = plugin.getServer().getClass().getPackage().getImplementationVersion();
        String version = pckg.split("\\.")[1].split("-")[0];
        int intVersion = Integer.parseInt(version);

        if (intVersion >= 15) {
            // brigadier enhancement
            plugin.getServer().getPluginManager().registerEvents(new PaperBrigadierEnhancement(),
                    plugin
            );
        } else {
            // 1.12, 1.13 and 1.14
            if (version[1] >= 12) {
                // async completions
                plugin.getServer().getPluginManager().registerEvents(new PaperAsyncTabEnhancement(),
                        plugin
                );
            }
        }
    }

}
