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

package me.lokka30.treasury.plugin.misc;

import me.lokka30.microlib.messaging.MessageUtils;
import me.lokka30.microlib.messaging.MicroLogger;
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.treasury.plugin.Treasury;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Utils {

    /**
     * This stores Treasury's MicroLogger, facilitating all console logs.
     */
    public static final MicroLogger logger = new MicroLogger("&b&lTreasury:&7 ");

    /**
     * @author lokka30
     * @since v1.0.0
     * Checks if the player has permission for the command.
     * If the player does not have permission then a notification
     * will be sent to them regarding their lack of permission.
     * @param main a link to the main class to access configuration files.
     * @param sender who ran a command and is being checked for the permission.
     * @param permission to check.
     * @return whether the sender has the specified permission.
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused"})
    public static boolean checkPermissionForCommand(@NotNull final Treasury main, @NotNull final CommandSender sender, @NotNull final String permission) {
        if(sender.hasPermission(permission)) {
            return true;
        } else {
            new MultiMessage(main.messagesCfg.getConfig().getStringList("common.no-permission"), Arrays.asList(
                            new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                            new MultiMessage.Placeholder("permission", permission, false)
            ));
            return false;
        }
    }

    /**
     * @author lokka30
     * @since v1.0.0
     * If the specified amount is less than zero
     * then zero is returned. Otherwise, the amount
     * is returned. This ensures an amount is at least zero
     * since negative values are not allowed in the API.
     * @param amount to check for.
     * @return the unmodified or modified amount.
     */
    public static double ensureAtLeastZero(final double amount) {
        return Math.max(amount, 0.0d);
    }

    @NotNull
    public static String getYesNoStateMessage(@NotNull final Treasury main, final boolean state) {
        if(state) {
            return main.messagesCfg.getConfig().getString("common.states.yes", "&aYes");
        } else {
            return main.messagesCfg.getConfig().getString("common.states.no", "&cNo");
        }
    }

    @NotNull
    public static String formatListMessage(@NotNull final Treasury main, @NotNull final List<String> list) {
        final String delimiter = MessageUtils.colorizeAll(main.messagesCfg.getConfig().getString("common.list-delimiter", "&7, &b"));

        return String.join(delimiter, list);
    }

}
