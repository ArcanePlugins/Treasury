/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.paper;

import java.util.logging.Level;
import me.lokka30.treasury.plugin.core.logging.Logger;
import org.bukkit.Bukkit;

import static me.lokka30.treasury.plugin.bukkit.BukkitTreasuryPlugin.colorize;

public class PaperDefaultLogger implements Logger {

    @Override
    public void info(final String message) {
        Bukkit.getConsoleSender().sendMessage("[Treasury] " + colorize(message));
    }

    @Override
    public void warn(final String message) {
        Bukkit.getConsoleSender().sendMessage(colorize("&e[WARNING] [Treasury] " + message));
    }

    @Override
    public void error(final String message) {
        Bukkit.getConsoleSender().sendMessage(colorize("&c[ERROR] [Treasury] " + message));
    }

    @Override
    public void error(final String message, final Throwable t) {
        Bukkit.getLogger().log(Level.SEVERE, "[Treasury]" + colorize(message), t);
    }

}
