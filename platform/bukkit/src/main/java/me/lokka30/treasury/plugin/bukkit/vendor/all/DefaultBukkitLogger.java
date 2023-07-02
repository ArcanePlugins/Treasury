/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.all;

import java.util.logging.Level;
import me.lokka30.treasury.plugin.core.logging.Logger;

import static me.lokka30.treasury.plugin.bukkit.BukkitTreasuryPlugin.colorize;

public class DefaultBukkitLogger implements Logger {

    private final java.util.logging.Logger wrapper;

    public DefaultBukkitLogger(final java.util.logging.Logger wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public void info(final String message) {
        wrapper.info(colorize(message));
    }

    @Override
    public void warn(final String message) {
        wrapper.warning(colorize(message));
    }

    @Override
    public void error(final String message) {
        wrapper.severe(colorize(message));
    }

    @Override
    public void error(final String message, final Throwable t) {
        wrapper.log(Level.SEVERE, colorize(message), t);
    }

}
