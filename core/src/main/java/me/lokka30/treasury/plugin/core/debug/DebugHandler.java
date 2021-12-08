/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.debug;

import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a handler for debugging.
 *
 * @author lokka30, MrIvanPlays
 * @since v1.0.0
 */
public class DebugHandler {

    /**
     * Returns whether the specified debug category is enabled.
     *
     * @param debugCategory the debug category to check if enabled
     * @return true if enabled, false otherwise
     */
    public static boolean isCategoryEnabled(@NotNull final DebugCategory debugCategory) {
        return TreasuryPlugin.getInstance()
                .configAdapter()
                .getSettings()
                .getDebugCategories()
                .contains(debugCategory);
    }

    /**
     * Appends a prefix to the message you want to debug.
     *
     * @param debugCategory the debug category you want to log message for
     * @param msg           the message you want to log
     */
    public static void log(@NotNull final DebugCategory debugCategory, @NotNull final String msg) {
        TreasuryPlugin.getInstance().logger().info("&8[&3DEBUG &8| &3" + debugCategory + "&8]: &7" + msg);
    }
}
