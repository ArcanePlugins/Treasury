/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.core.debug;

import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.settings.SettingKey;
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
                .getSetting(SettingKey.DEBUG_CATEGORY_LIST)
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
