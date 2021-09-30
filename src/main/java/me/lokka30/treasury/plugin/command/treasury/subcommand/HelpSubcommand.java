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

package me.lokka30.treasury.plugin.command.treasury.subcommand;

import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.command.Subcommand;
import me.lokka30.treasury.plugin.misc.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpSubcommand implements Subcommand {

    /*
    inf: View the plugin's available commands.
    cmd: /treasury help
    arg:         |    0
    len:         0    1
     */

    @NotNull private final Treasury main;
    public HelpSubcommand(@NotNull final Treasury main) { this.main = main; }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(!Utils.checkPermissionForCommand(main, sender, "treasury.command.treasury.help")) return;

        if(args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid usage, try '/" + label + " help'.");
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "Available commands:");
        sender.sendMessage(ChatColor.GRAY + " -> /" + label + " help - view a list of Treasury's commands");
        sender.sendMessage(ChatColor.GRAY + " -> /" + label + " info - view info about Treasury and the Provider");
        sender.sendMessage(ChatColor.GRAY + " -> /" + label + " migrate - migrate from one Provider to another");
    }
}
