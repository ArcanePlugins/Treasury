/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.paper;

import me.lokka30.treasury.plugin.core.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PaperComponentLogger implements Logger {

    private final ComponentLogger wrapper;

    public PaperComponentLogger(final ComponentLogger wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public void info(final String message) {
        wrapper.info(colorize(message));
    }

    @Override
    public void warn(final String message) {
        wrapper.warn(colorize(message));
    }

    @Override
    public void error(final String message) {
        wrapper.error(colorize(message));
    }

    @Override
    public void error(final String message, final Throwable t) {
        wrapper.error(colorize(message), t);
    }

    private Component colorize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

}
