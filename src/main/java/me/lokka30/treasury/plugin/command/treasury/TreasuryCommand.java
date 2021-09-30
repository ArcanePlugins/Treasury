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

package me.lokka30.treasury.plugin.command.treasury;

import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.command.treasury.subcommand.HelpSubcommand;
import me.lokka30.treasury.plugin.command.treasury.subcommand.InfoSubcommand;
import me.lokka30.treasury.plugin.command.treasury.subcommand.MigrateSubcommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TreasuryCommand implements TabExecutor {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    @NotNull private final Treasury main;
    public TreasuryCommand(@NotNull final Treasury main) {
        this.main = main;

        this.helpSubcommand = new HelpSubcommand(main);
        this.infoSubcommand = new InfoSubcommand(main);
        this.migrateSubcommand = new MigrateSubcommand(main);
    }

    @NotNull private final HelpSubcommand helpSubcommand;
    @NotNull private final InfoSubcommand infoSubcommand;
    @NotNull private final MigrateSubcommand migrateSubcommand;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid subcommand. For a list of available subcommands, run '/" + label + " help'."); // TODO
            return true;
        } else {
            switch(args[0].toUpperCase(Locale.ROOT)) {
                case "HELP":
                    helpSubcommand.run(sender, label, args);
                    return true;
                case "INFO":
                    infoSubcommand.run(sender, label, args);
                    return true;
                case "MIGRATE":
                    migrateSubcommand.run(sender, label, args);
                    return true;
                default:
                    sender.sendMessage(ChatColor.RED + "Invalid subcommand '" + args[0] + "', run '/" + label + " help' for a list of available subcommands."); // TODO
                    return true;
            }
        }
    }

    @NotNull private final List<String> subcommands = Arrays.asList("help", "info", "migrate");

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            return Collections.emptyList();
        } else if(args.length == 1) {
            return subcommands;
        } else {
            switch(args[0].toUpperCase(Locale.ROOT)) {
                case "HELP":
                    return helpSubcommand.getTabSuggestions(sender, label, args);
                case "INFO":
                    return infoSubcommand.getTabSuggestions(sender, label, args);
                case "MIGRATE":
                    return migrateSubcommand.getTabSuggestions(sender, label, args);
                default:
                    return Collections.emptyList();
            }
        }
    }
}
