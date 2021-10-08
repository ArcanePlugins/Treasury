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

package me.lokka30.treasury.plugin.command.treasury;

import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.command.treasury.subcommand.HelpSubcommand;
import me.lokka30.treasury.plugin.command.treasury.subcommand.InfoSubcommand;
import me.lokka30.treasury.plugin.command.treasury.subcommand.MigrateSubcommand;
import me.lokka30.treasury.plugin.command.treasury.subcommand.ReloadSubcommand;
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
        this.reloadSubcommand = new ReloadSubcommand(main);
    }

    @NotNull private final HelpSubcommand helpSubcommand;
    @NotNull private final InfoSubcommand infoSubcommand;
    @NotNull private final MigrateSubcommand migrateSubcommand;
    @NotNull private final ReloadSubcommand reloadSubcommand;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid subcommand. For a list of available subcommands, run '/" + label + " help'.");
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
                case "RELOAD":
                    reloadSubcommand.run(sender, label, args);
                    return true;
                default:
                    sender.sendMessage(ChatColor.RED + "Invalid subcommand '" + args[0] + "', run '/" + label + " help' for a list of available subcommands.");
                    return true;
            }
        }
    }

    @NotNull private final List<String> subcommands = Arrays.asList("help", "info", "migrate", "reload");

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
                case "RELOAD":
                    return reloadSubcommand.getTabSuggestions(sender, label, args);
                default:
                    return Collections.emptyList();
            }
        }
    }
}
