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

import me.lokka30.microlib.maths.QuickTimer;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.exception.*;
import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.command.Subcommand;
import me.lokka30.treasury.plugin.misc.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
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

        sender.sendMessage(ChatColor.GRAY + "Starting migration, please wait (this may briefly lag the server)...");
        final QuickTimer timer = new QuickTimer();

        int playerAccountsProcessed = 0;
        int nonPlayerAccountsProcessed = 0;
        int bankAccountsProcessed = 0;

        HashMap<String, Currency> migratedCurrencies = new HashMap<>();
        HashSet<String> nonMigratedCurrencies = new HashSet<>();

        try {
            for(String currencyId : from.getCurrencyNames()) {
                if(to.getCurrencyNames().contains(currencyId)) {
                    migratedCurrencies.put(currencyId, to.getCurrency(currencyId));
                } else {
                    nonMigratedCurrencies.add(currencyId);
                }
            }
        } catch(InvalidCurrencyException ex) {
            // this should be impossible
            sender.sendMessage(ChatColor.RED + "An internal error occured: " + ex.getMessage());
            return;
        }

        /* Migrate player accounts */
        try {
            for(UUID uuid : from.getPlayerAccountIds()) {
                if(!to.hasPlayerAccount(uuid)) {
                    to.createPlayerAccount(uuid);
                }

                for(String currencyId : migratedCurrencies.keySet()) {
                    final BigDecimal balance = Utils.ensureAtLeastZero(from.getPlayerAccount(uuid).getBalance("", from.getCurrency(currencyId)));

                    from.getPlayerAccount(uuid).withdrawBalance(balance, "", from.getCurrency(currencyId));
                    to.getPlayerAccount(uuid).depositBalance(balance, "", to.getCurrency(currencyId));
                }

                playerAccountsProcessed++;
            }
        } catch(AccountAlreadyExistsException | InvalidCurrencyException | InvalidAmountException | OversizedWithdrawalException ex) {
            // this should be impossible
            sender.sendMessage(ChatColor.RED + "An internal error occured: " + ex.getMessage());
            return;
        }

        /* Migrate non-player accounts */
        if(from.hasNonPlayerAccountSupport() && to.hasNonPlayerAccountSupport()) {
            try {
                for(UUID uuid : from.getNonPlayerAccountIds()) {
                    if(!to.hasNonPlayerAccount(uuid)) {
                        to.createNonPlayerAccount(uuid);
                    }

                    for(String currencyId : migratedCurrencies.keySet()) {
                        final BigDecimal balance = Utils.ensureAtLeastZero(from.getNonPlayerAccount(uuid).getBalance("", from.getCurrency(currencyId)));

                        from.getNonPlayerAccount(uuid).withdrawBalance(balance, "", from.getCurrency(currencyId));
                        to.getNonPlayerAccount(uuid).depositBalance(balance, "", to.getCurrency(currencyId));
                    }

                    nonPlayerAccountsProcessed++;
                }
            } catch(UnsupportedEconomyFeatureException | InvalidCurrencyException | AccountAlreadyExistsException | InvalidAmountException | OversizedWithdrawalException ex) {
                // this should be impossible
                sender.sendMessage(ChatColor.RED + "An internal error occured: " + ex.getMessage());
                return;
            }
        }

        /* Migrate bank accounts */
        if(from.hasBankAccountSupport() && to.hasBankAccountSupport()) {
            try {
                for(UUID uuid : from.getBankAccountIds()) {
                    if(!to.hasBankAccount(uuid)) {
                        to.createBankAccount(uuid, from.getBankAccount(uuid).getOwningPlayerId());
                    }

                    for(String currencyId : migratedCurrencies.keySet()) {
                        final BigDecimal balance = Utils.ensureAtLeastZero(from.getBankAccount(uuid).getBalance("", from.getCurrency(currencyId)));

                        from.getBankAccount(uuid).withdrawBalance(balance, "", from.getCurrency(currencyId));
                        to.getBankAccount(uuid).depositBalance(balance, "", to.getCurrency(currencyId));
                    }

                    bankAccountsProcessed++;
                }
            } catch(AccountAlreadyExistsException | UnsupportedEconomyFeatureException | InvalidCurrencyException | InvalidAmountException | OversizedWithdrawalException ex) {
                // this should be impossible
                sender.sendMessage(ChatColor.RED + "An internal error occured: " + ex.getMessage());
                return;
            }
        }

        sender.sendMessage(ChatColor.GREEN + "Migration complete!");
        sender.sendMessage(ChatColor.GRAY + "Statistics:");
        sender.sendMessage(ChatColor.GRAY + "- took " + timer.getTimer() + "ms");
        sender.sendMessage(ChatColor.GRAY + "- player accounts processed: " + playerAccountsProcessed);
        sender.sendMessage(ChatColor.GRAY + "- nonPlayerAccountsProcessed accounts processed: " + nonPlayerAccountsProcessed);
        sender.sendMessage(ChatColor.GRAY + "- bank accounts processed: " + bankAccountsProcessed);
        sender.sendMessage(ChatColor.GRAY + "- migrated currencies: " + String.join(", ", migratedCurrencies.keySet()));
        sender.sendMessage(ChatColor.GRAY + "- unmigrated currencies: " + String.join(", ", nonMigratedCurrencies));
    }
}
