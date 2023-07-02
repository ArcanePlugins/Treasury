/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.paper;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.logging.Logger;
import org.bukkit.plugin.Plugin;

public class PaperLogger {

    private static Logger instance;

    public static Logger getLogger(TreasuryBukkit plugin) {
        if (instance != null) {
            return instance;
        }
        try {
            Plugin.class.getDeclaredMethod("getComponentLogger");
            return instance = new PaperComponentLogger(plugin.getComponentLogger());
        } catch (NoSuchMethodException e) {
            return instance = new PaperDefaultLogger();
        }
    }

}
