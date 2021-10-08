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

import me.lokka30.microlib.maths.QuickTimer;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.exception.*;
import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.command.Subcommand;
import me.lokka30.treasury.plugin.debug.DebugCategory;
import me.lokka30.treasury.plugin.misc.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MigrateSubcommand implements Subcommand {

    /*
    inf: Migrates accounts from one economy plugin to another
    cmd: /treasury migrate <providerFrom> <providerTo>
    arg:         |       0              1            2
    len:         0       1              2            3
     */

    @NotNull private final Treasury main;
    public MigrateSubcommand(@NotNull final Treasury main) { this.main = main; }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        final boolean debugEnabled = main.debugHandler.isCategoryEnabled(DebugCategory.MIGRATE_SUBCOMMAND);

        if(!Utils.checkPermissionForCommand(main, sender, "treasury.command.treasury.migrate")) return;

        if(args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Invalid usage, try '/" + label + " migrate <providerFrom> <providerTo>'.");
            return;
        }

        Collection<RegisteredServiceProvider<EconomyProvider>> serviceProviders = main.getServer().getServicesManager().getRegistrations(EconomyProvider.class);
        EconomyProvider from = null;
        EconomyProvider to = null;

        if(serviceProviders.size() < 2) {
            sender.sendMessage(ChatColor.RED + "You require at least 2 Economy providers to run this subcommand.");
            return;
        }

        final HashSet<String> serviceProvidersNames = new HashSet<>();

        for(RegisteredServiceProvider<EconomyProvider> serviceProvider : serviceProviders) {
            serviceProvidersNames.add(serviceProvider.getPlugin().getName());
            if(debugEnabled) {
                main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Found service provider: " + serviceProvider.getPlugin().getName());
            }
        }

        if(args[1].equalsIgnoreCase(args[2])) {
            sender.sendMessage(ChatColor.RED + "You must specify two different economy providers.");
            sender.sendMessage(ChatColor.RED + "Valid service providers: " + String.join(", ", serviceProvidersNames));
            return;
        }

        for(RegisteredServiceProvider<EconomyProvider> serviceProvider : serviceProviders) {
            final String serviceProviderPluginName = serviceProvider.getPlugin().getName();

            if(args[1].equalsIgnoreCase(serviceProviderPluginName)) {
                from = serviceProvider.getProvider();
            } else if(args[2].equalsIgnoreCase(serviceProviderPluginName)) {
                to = serviceProvider.getProvider();
            }
        }

        if(from == null) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid economy provider to migrate from.");
            sender.sendMessage(ChatColor.RED + "Valid service providers: " + String.join(", ", serviceProvidersNames));
            return;
        }

        if(to == null) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid economy provider to migrate to.");
            sender.sendMessage(ChatColor.RED + "Valid service providers: " + String.join(", ", serviceProvidersNames));
            return;
        }

        if(debugEnabled) {
            main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Migrating from '&b" + from.getProvider().getName() + "&7' to '&b" + to.getProvider().getName() + "&7'.");
        }

        sender.sendMessage(ChatColor.GRAY + "Starting migration, please wait (this may briefly lag the server)...");
        final QuickTimer timer = new QuickTimer();

        int playerAccountsProcessed = 0;
        int nonPlayerAccountsProcessed = 0;
        int bankAccountsProcessed = 0;

        HashMap<String, Currency> migratedCurrencies = new HashMap<>();
        HashSet<String> nonMigratedCurrencies = new HashSet<>();

        for(String currencyId : from.getCurrencyNames()) {
            if(to.getCurrencyNames().contains(currencyId)) {
                try {
                    migratedCurrencies.put(currencyId, to.getCurrency(currencyId));
                } catch(InvalidCurrencyException ex) {
                    // this should be impossible
                    sender.sendMessage(ChatColor.RED + "An internal error occured whilst processing currency of ID '&b" + currencyId + "&7': " + ex.getMessage());
                    continue;
                }

                if(debugEnabled) {
                    main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Currency of ID '&b" + currencyId + "&7' will be migrated.");
                }
            } else {
                nonMigratedCurrencies.add(currencyId);

                if(debugEnabled) {
                    main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Currency of ID '&b" + currencyId + "&7' will not be migrated.");
                }
            }
        }

        /* Migrate player accounts */
        try {
            for(UUID uuid : from.getPlayerAccountIds()) {
                if(debugEnabled) main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Migrating player account of UUID '&b" + uuid + "&7'.");

                if(!to.hasPlayerAccount(uuid)) {
                    to.createPlayerAccount(uuid);
                }

                for(String currencyId : migratedCurrencies.keySet()) {
                    final double balance = Utils.ensureAtLeastZero(from.getPlayerAccount(uuid).getBalance("", from.getCurrency(currencyId)));

                    from.getPlayerAccount(uuid).withdrawBalance(balance, "", from.getCurrency(currencyId));
                    to.getPlayerAccount(uuid).depositBalance(balance, "", to.getCurrency(currencyId));
                }

                playerAccountsProcessed++;
            }
        } catch(AccountAlreadyExistsException | InvalidCurrencyException | InvalidAmountException | OversizedWithdrawalException ex) {
            // these should be impossible
            sender.sendMessage(ChatColor.RED + "An internal error occured: " + ex.getMessage());
            return;
        }

        /* Migrate non-player accounts */
        if(from.hasNonPlayerAccountSupport() && to.hasNonPlayerAccountSupport()) {
            try {
                for(UUID uuid : from.getNonPlayerAccountIds()) {
                    if(debugEnabled) main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Migrating non-player account of UUID '&b" + uuid + "&7'.");

                    if(!to.hasNonPlayerAccount(uuid)) {
                        to.createNonPlayerAccount(uuid);
                    }

                    for(String currencyId : migratedCurrencies.keySet()) {
                        final double balance = Utils.ensureAtLeastZero(from.getNonPlayerAccount(uuid).getBalance("", from.getCurrency(currencyId)));

                        from.getNonPlayerAccount(uuid).withdrawBalance(balance, "", from.getCurrency(currencyId));
                        to.getNonPlayerAccount(uuid).depositBalance(balance, "", to.getCurrency(currencyId));
                    }

                    nonPlayerAccountsProcessed++;
                }
            } catch(UnsupportedEconomyFeatureException | InvalidCurrencyException | AccountAlreadyExistsException | InvalidAmountException | OversizedWithdrawalException ex) {
                // these should be impossible
                sender.sendMessage(ChatColor.RED + "An internal error occured: " + ex.getMessage());
                return;
            }
        }

        /* Migrate bank accounts */
        if(from.hasBankAccountSupport() && to.hasBankAccountSupport()) {
            try {
                for(UUID uuid : from.getBankAccountIds()) {
                    if(debugEnabled) main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Migrating bank account of UUID '&b" + uuid + "&7'.");

                    if(!to.hasBankAccount(uuid)) {
                        to.createBankAccount(uuid, from.getBankAccount(uuid).getOwningPlayerId());
                    }

                    for(String currencyId : migratedCurrencies.keySet()) {
                        final double balance = Utils.ensureAtLeastZero(from.getBankAccount(uuid).getBalance("", from.getCurrency(currencyId)));

                        from.getBankAccount(uuid).withdrawBalance(balance, "", from.getCurrency(currencyId));
                        to.getBankAccount(uuid).depositBalance(balance, "", to.getCurrency(currencyId));
                    }

                    bankAccountsProcessed++;
                }
            } catch(AccountAlreadyExistsException | UnsupportedEconomyFeatureException | InvalidCurrencyException | InvalidAmountException | OversizedWithdrawalException ex) {
                // these should be impossible
                sender.sendMessage(ChatColor.RED + "An internal error occured: " + ex.getMessage());
                return;
            }
        }

        sender.sendMessage(ChatColor.GREEN + "Migration complete!");
        sender.sendMessage(ChatColor.GRAY + "Statistics:");
        sender.sendMessage(ChatColor.GRAY + " - took " + timer.getTimer() + "ms");
        sender.sendMessage(ChatColor.GRAY + " - player accounts processed: " + playerAccountsProcessed);
        sender.sendMessage(ChatColor.GRAY + " - nonPlayerAccountsProcessed accounts processed: " + nonPlayerAccountsProcessed);
        sender.sendMessage(ChatColor.GRAY + " - bank accounts processed: " + bankAccountsProcessed);
        sender.sendMessage(ChatColor.GRAY + " - migrated currencies: " + String.join(", ", migratedCurrencies.keySet()));
        sender.sendMessage(ChatColor.GRAY + " - unmigrated currencies: " + String.join(", ", nonMigratedCurrencies));
    }
}
