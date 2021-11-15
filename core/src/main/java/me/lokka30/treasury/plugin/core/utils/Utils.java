/*
 * Copyright (c) 2021-2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.core.utils;

import java.util.Objects;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.config.messaging.ColorHandler;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import org.jetbrains.annotations.NotNull;

public class Utils {

    /**
     * Checks if the player has permission for the command.
     * If the player does not have permission then a notification
     * will be sent to them regarding their lack of permission.
     *
     * @param source     who ran a command and is being checked for the permission.
     * @param permission to check.
     * @return whether the sender has the specified permission.
     * @author lokka30
     * @since v1.0.0
     */
    public static boolean checkPermissionForCommand(@NotNull CommandSource source, @NotNull String permission) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(permission, "permission");
        if (source.hasPermission(permission)) {
            return true;
        } else {
            source.sendMessage(
                    Message.of(
                            MessageKey.NO_PERMISSION,
                            MessagePlaceholder.placeholder("%permission%", permission)
                    )
            );
            return false;
        }
    }

    @NotNull
    public static String getYesNoStateMessage(final boolean state) {
        Messages messages = TreasuryPlugin.getInstance().configAdapter().getMessages();
        ColorHandler colorHandler = TreasuryPlugin.getInstance().colorHandler();
        return state
                ? colorHandler.colorize(messages.getSingleMessage(MessageKey.STATE_YES))
                : colorHandler.colorize(messages.getSingleMessage(MessageKey.STATE_NO));
    }

    @NotNull
    public static String formatListMessage(@NotNull final Iterable<String> list) {
        Objects.requireNonNull(list, "list");
        final String delimiter = TreasuryPlugin.getInstance().colorHandler().colorize(
                TreasuryPlugin.getInstance().configAdapter().getMessages().getSingleMessage(MessageKey.LIST_DELIMITER)
        );

        return String.join(delimiter, list);
    }

}
