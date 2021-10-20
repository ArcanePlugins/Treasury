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
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.command.Subcommand;
import me.lokka30.treasury.plugin.debug.DebugCategory;
import me.lokka30.treasury.plugin.misc.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.*;

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
            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.migrate.invalid-usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ));
            return;
        }

        Collection<RegisteredServiceProvider<EconomyProvider>> serviceProviders = main.getServer().getServicesManager().getRegistrations(EconomyProvider.class);
        EconomyProvider from = null;
        EconomyProvider to = null;

        if(serviceProviders.size() < 2) {
            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.migrate.requires-two-providers"), Collections.singletonList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true)
            ));
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
            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.migrate.providers-match"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                    new MultiMessage.Placeholder("providers", Utils.formatListMessage(main, new ArrayList<>(serviceProvidersNames)), false)
            ));
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
            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.migrate.requires-valid-from"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                    new MultiMessage.Placeholder("providers", Utils.formatListMessage(main, new ArrayList<>(serviceProvidersNames)), false)
            ));
            return;
        }

        if(to == null) {
            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.migrate.requires-valid-to"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                    new MultiMessage.Placeholder("providers", Utils.formatListMessage(main, new ArrayList<>(serviceProvidersNames)), false)
            ));
            return;
        }

        if(debugEnabled) {
            main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Migrating from '&b" + from.getProvider().getName() + "&7' to '&b" + to.getProvider().getName() + "&7'.");
        }

        new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.migrate.starting-migration"), Collections.singletonList(
                new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true)
        ));

        final QuickTimer timer = new QuickTimer();

        AtomicInteger playerAccountsProcessed = new AtomicInteger();
        AtomicInteger bankAccountsProcessed = new AtomicInteger();

        Map<Currency, Currency> migratedCurrencies = new HashMap<>();
        Collection<String> nonMigratedCurrencies = new ArrayList<>();
        for (UUID uuid : from.getCurrencyIds()) {
            Currency fromCurrency = from.getCurrency(uuid);

            if (fromCurrency == null) {
                // Shouldn't be possible unless from implementation is severely broken.
                if (debugEnabled) {
                    main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Unable to locate reported currency with ID '&b" + uuid + "&7'.");
                }
                continue;
            }

            Currency toCurrency = to.getCurrency(fromCurrency.getCurrencyName());

            if (toCurrency == null) {
                if(debugEnabled) {
                    main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Currency of ID '&b" + fromCurrency.getCurrencyName() + "&7' will not be migrated.");
                }
            } else {
                migratedCurrencies.put(fromCurrency, toCurrency);
                if(debugEnabled) {
                    main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Currency of ID '&b" + fromCurrency.getCurrencyName() + "&7' will be migrated.");
                }
            }
        }

        if (migratedCurrencies.isEmpty()) {
            // Nothing to migrate. Maybe a special message?
            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.migrate.finished-migration"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                    new MultiMessage.Placeholder("time", timer.getTimer() + "", false),
                    new MultiMessage.Placeholder("player-accounts", playerAccountsProcessed.toString(), false),
                    new MultiMessage.Placeholder("bank-accounts", bankAccountsProcessed.toString(), false),
                    new MultiMessage.Placeholder("migrated-currencies", Utils.formatListMessage(main, migratedCurrencies.keySet().stream().map(Currency::getCurrencyName).collect(Collectors.toList())), false),
                    new MultiMessage.Placeholder("non-migrated-currencies", Utils.formatListMessage(main, nonMigratedCurrencies), false)
            )).send(sender);
            return;
        }

        // Initialize phaser with a single party which will be our async task awaiting migration completion.
        Phaser playerMigration = new Phaser(1);
        migratePlayerAccounts(playerMigration, from, to, migratedCurrencies, playerAccountsProcessed, debugEnabled);

        /* TODO Migrate bank accounts similarly
        if(from.hasBankAccountSupport() && to.hasBankAccountSupport()) {
            for(UUID uuid : from.getBankAccountIds()) {
                if(debugEnabled) main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Migrating bank account of UUID '&b" + uuid + "&7'.");

                if(!to.hasBankAccount(uuid).get()) {
                    to.createBankAccount(uuid);

                    for(UUID ownerId : from.getBankAccount(uuid).get().getBankOwnersIds()) {
                        to.getBankAccount(uuid).get().addBankOwner(ownerId);
                    }

                    for(UUID memberId : from.getBankAccount(uuid).get().getBankMembersIds()) {
                        to.getBankAccount(uuid).get().addBankMember(memberId);
                    }
                }

                for(String currencyId : migratedCurrencies.keySet()) {
                    final double balance = Utils.ensureAtLeastZero(from.getBankAccount(uuid).get().getBalance(null, Objects.requireNonNull(from.getCurrency(currencyId))));

                    from.getBankAccount(uuid).get().withdrawBalance(balance, null, Objects.requireNonNull(from.getCurrency(currencyId)));
                    to.getBankAccount(uuid).get().depositBalance(balance, null, Objects.requireNonNull(to.getCurrency(currencyId)));
                }

                bankAccountsProcessed++;
            }
        } */

        main.getServer().getScheduler().runTaskAsynchronously(main, () -> {
            // Block until player migration is complete.
            playerMigration.arriveAndAwaitAdvance();

            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.migrate.finished-migration"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                    new MultiMessage.Placeholder("time", timer.getTimer() + "", false),
                    new MultiMessage.Placeholder("player-accounts", playerAccountsProcessed.toString(), false),
                    new MultiMessage.Placeholder("bank-accounts", bankAccountsProcessed.toString(), false),
                    new MultiMessage.Placeholder("migrated-currencies", Utils.formatListMessage(main, migratedCurrencies.keySet().stream().map(Currency::getCurrencyName).collect(Collectors.toList())), false),
                    new MultiMessage.Placeholder("non-migrated-currencies", Utils.formatListMessage(main, nonMigratedCurrencies), false)
            )).send(sender);
        });
    }

    private void migratePlayerAccounts(
            @NotNull Phaser phaser,
            @NotNull EconomyProvider from,
            @NotNull EconomyProvider to,
            @NotNull Map<Currency, Currency> migrated,
            @NotNull AtomicInteger playerAccountsProcessed,
            boolean debugEnabled) {

        from.requestPlayerAccountIds(new PhasedSubscriber<Collection<UUID>>(phaser) {
            @Override
            public void phaseAccept(@NotNull Collection<UUID> uuids) {
                for (UUID uuid : uuids) {
                    migratePlayerAccount(phaser, uuid, from, to, migrated, playerAccountsProcessed, debugEnabled);
                }
            }

            @Override
            public void phaseError(@NotNull EconomyException exception) {
                if (debugEnabled) {
                    main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Unable to fetch player account UUIDs for migration: " + exception.getMessage());
                }
            }
        });
    }

    private void migratePlayerAccount(
            @NotNull Phaser phaser,
            @NotNull UUID uuid,
            @NotNull EconomyProvider from,
            @NotNull EconomyProvider to,
            @NotNull Map<Currency, Currency> migrated,
            @NotNull AtomicInteger playerAccountsProcessed,
            boolean debugEnabled) {
        if (debugEnabled) {
            main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Migrating player account of UUID '&b" + uuid + "&7'.");
        }

        CompletableFuture<PlayerAccount> fromAccountFuture = new CompletableFuture<>();

        from.requestPlayerAccount(uuid, new PlayerSubscriber<PlayerAccount>(phaser, uuid, fromAccountFuture, debugEnabled) {
            @Override
            public void phaseAccept(@NotNull PlayerAccount playerAccount) {
                fromAccountFuture.complete(playerAccount);
            }
        });

        CompletableFuture<PlayerAccount> toAccountFuture = new CompletableFuture<>();

        to.hasPlayerAccount(uuid, new PlayerSubscriber<Boolean>(phaser, uuid, toAccountFuture, debugEnabled) {
            @Override
            public void phaseAccept(@NotNull Boolean hasAccount) {
                PlayerSubscriber<PlayerAccount> subscription = new PlayerSubscriber<PlayerAccount>(phaser, uuid, toAccountFuture, debugEnabled) {
                    @Override
                    public void phaseAccept(@NotNull PlayerAccount playerAccount) {
                        toAccountFuture.complete(playerAccount);
                    }
                };
                if (hasAccount) {
                    to.requestPlayerAccount(uuid, subscription);
                } else {
                    to.createPlayerAccount(uuid, subscription);
                }
            }
        });

        fromAccountFuture.thenAcceptBoth(toAccountFuture, (fromAccount, toAccount) -> {
            for (Map.Entry<Currency, Currency> currencyEntry : migrated.entrySet()) {
                safeTransferAll(phaser, fromAccount, toAccount, currencyEntry.getKey(), currencyEntry.getValue(), debugEnabled);
            }
            playerAccountsProcessed.incrementAndGet();
        });
    }

    private void safeTransferAll(
            @NotNull Phaser phaser,
            @NotNull Account fromAccount,
            @NotNull Account toAccount,
            @NotNull Currency fromCurrency,
            @NotNull Currency toCurrency,
            boolean debugEnabled) {
        CompletableFuture<Double> balanceFuture = new CompletableFuture<>();

        fromAccount.requestBalance(fromCurrency, new PhasedSubscriber<Double>(phaser) {
            @Override
            public void phaseAccept(@NotNull Double balance) {
                balanceFuture.complete(balance);
            }

            @Override
            public void phaseError(@NotNull EconomyException exception) {
                if (debugEnabled) {
                    main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Error migrating account of UUID '&b" + fromAccount.getUniqueId() + "&7': &b" + exception.getMessage());
                }
                balanceFuture.completeExceptionally(exception);
            }
        });

        balanceFuture.thenAccept(balance -> {
            if (balance == 0) {
                return;
            }
            EconomySubscriber<Double> subscriber = new PhasedSubscriber<Double>(phaser) {
                @Override
                public void phaseAccept(@NotNull Double newBalance) {
                    // Don't add extra logging for success.
                }

                @Override
                public void phaseError(@NotNull EconomyException exception) {
                    if (debugEnabled) {
                        main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Error migrating account of UUID '&b" + fromAccount.getUniqueId() + "&7': &b" + exception.getMessage());
                    }
                }
            };
            if (balance < 0) {
                toAccount.withdrawBalance(balance, toCurrency, subscriber);
            } else {
                toAccount.depositBalance(balance, toCurrency, subscriber);
            }
        });
    }

    private abstract static class PhasedSubscriber<T> implements EconomySubscriber<T> {

        private final Phaser phaser;

        private PhasedSubscriber(@NotNull Phaser phaser) {
            this.phaser = phaser;
            this.phaser.register();
        }

        @Override
        public final void succeed(@NotNull T t) {
            phaseAccept(t);
            phaser.arriveAndDeregister();
        }

        public abstract void phaseAccept(@NotNull T t);

        @Override
        public final void fail(@NotNull EconomyException exception) {
            phaseError(exception);
            phaser.arriveAndDeregister();
        }

        public abstract void phaseError(@NotNull EconomyException exception);

    }

    private abstract class PlayerSubscriber<T> extends PhasedSubscriber<T> {
        private final UUID uuid;
        private final CompletableFuture<PlayerAccount> accountFuture;
        private final boolean debugEnabled;

        private PlayerSubscriber(
                @NotNull Phaser phaser,
                @NotNull UUID uuid,
                @NotNull CompletableFuture<PlayerAccount> accountFuture,
                boolean debugEnabled) {
            super(phaser);
            this.uuid = uuid;
            this.accountFuture = accountFuture;
            this.debugEnabled = debugEnabled;
        }

        @Override
        public final void phaseError(@NotNull EconomyException exception) {
            if (debugEnabled) {
                main.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Error migrating account of player UUID '&b" + uuid + "&7': &b" + exception.getMessage());
            }
            accountFuture.completeExceptionally(exception);
        }
    }

}
