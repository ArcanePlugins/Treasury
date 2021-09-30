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

import me.lokka30.treasury.api.EconomyProvider;
import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.command.Subcommand;
import me.lokka30.treasury.plugin.misc.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class InfoSubcommand implements Subcommand {

    /*
    inf: Prints generic information about the plugin.
    cmd: /treasury info
    arg:         |    0
    len:         0    1
     */

    @NotNull private final Treasury main;
    public InfoSubcommand(@NotNull final Treasury main) { this.main = main; }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(!Utils.checkPermissionForCommand(main, sender, "treasury.command.treasury.info")) return;

        if(args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid usage, try '/" + label + " info'.");
            return;
        }

        final RegisteredServiceProvider<EconomyProvider> registeredServiceProvider = main.getServer().getServicesManager().getRegistration(EconomyProvider.class);

        sender.sendMessage(ChatColor.WHITE + "" + ChatColor.UNDERLINE + "About Treasury");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GRAY + "Plugin:");
        sender.sendMessage(ChatColor.GRAY + " - Running: " + main.getDescription().getName() + " v" + main.getDescription().getVersion());
        sender.sendMessage(ChatColor.GRAY + " - Description: " + main.getDescription().getDescription());
        sender.sendMessage(ChatColor.GRAY + " - Contributors: " + String.join(", ", Treasury.contributors));
        sender.sendMessage(ChatColor.GRAY + " - API Version: " + Treasury.apiVersion);
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GRAY + "Current Provider:");
        if(registeredServiceProvider == null) {
            sender.sendMessage(ChatColor.RED + " - No valid Economy provider is installed!");
        } else {
            EconomyProvider provider = registeredServiceProvider.getProvider();

            sender.sendMessage(ChatColor.GRAY + " - Name: " + provider.getProvider().getName());
            sender.sendMessage(ChatColor.GRAY + " - API Version: " + provider.getSupportedAPIVersion());
            sender.sendMessage(ChatColor.GRAY + " - Supports non-player accounts: " + provider.hasNonPlayerAccountSupport());
            sender.sendMessage(ChatColor.GRAY + " - Supports bank accounts: " + provider.hasBankAccountSupport());
            sender.sendMessage(ChatColor.GRAY + " - Supports per-world balances: " + provider.hasPerWorldBalanceSupport());
            sender.sendMessage(ChatColor.GRAY + " - Supports transaction events: " + provider.hasTransactionEventSupport());
            sender.sendMessage(ChatColor.GRAY + " - Primary currency ID: " + provider.getPrimaryCurrency().getCurrencyId());
        }
    }
}
