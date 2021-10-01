/*
 * Copyright (c) 2021 lokka30.
 * This code is part of Treasury, an Economy API for Minecraft servers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You have received a copy of the GNU Affero General Public License
 * with this program - please see the LICENSE.md file. Alternatively,
 * please visit the <https://www.gnu.org/licenses/> website.
 *
 * Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 */

package me.lokka30.treasury.plugin.misc;

import me.lokka30.microlib.messaging.MicroLogger;
import me.lokka30.treasury.plugin.Treasury;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

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
            sender.sendMessage(ChatColor.RED + "You don't have access to that (requires permission " + permission + ").");
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
    @NotNull
    public static BigDecimal ensureAtLeastZero(@NotNull final BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        } else {
            return amount;
        }
    }

}
