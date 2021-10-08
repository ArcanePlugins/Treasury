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

package me.lokka30.treasury.plugin.command.treasury.subcommand;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.exception.InvalidCurrencyException;
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
        sender.sendMessage(ChatColor.GRAY + " - Learn more at https://github.com/lokka30/Treasury/");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GRAY + "Current Provider:");
        if(registeredServiceProvider == null) {
            sender.sendMessage(ChatColor.RED + " - No valid Economy provider is installed!");
        } else {
            EconomyProvider provider = registeredServiceProvider.getProvider();

            sender.sendMessage(ChatColor.GRAY + " - Name: " + provider.getProvider().getName());
            sender.sendMessage(ChatColor.GRAY + " - Priority: " + registeredServiceProvider.getPriority());
            sender.sendMessage(ChatColor.GRAY + " - Supported API Version: " + provider.getSupportedAPIVersion());
            sender.sendMessage(ChatColor.GRAY + " - Supports non-player accounts: " + provider.hasNonPlayerAccountSupport());
            sender.sendMessage(ChatColor.GRAY + " - Supports bank accounts: " + provider.hasBankAccountSupport());
            sender.sendMessage(ChatColor.GRAY + " - Supports per-world balances: " + provider.hasPerWorldBalanceSupport());
            sender.sendMessage(ChatColor.GRAY + " - Supports transaction events: " + provider.hasTransactionEventSupport());
            try {
                sender.sendMessage(ChatColor.GRAY + " - Primary currency ID: " + provider.getPrimaryCurrency().getCurrencyName());
            } catch(InvalidCurrencyException ex) {
                sender.sendMessage(ChatColor.RED + " - (Unable to get primary currency - currency is invalid!)");
            }
        }
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GRAY + "Miscellaneous Info:");
        sender.sendMessage(ChatColor.GRAY + " - For command help, try '/treasury help'.");
    }
}
