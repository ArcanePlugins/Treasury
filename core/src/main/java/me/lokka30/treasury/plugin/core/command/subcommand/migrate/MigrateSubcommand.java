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

package me.lokka30.treasury.plugin.core.command.subcommand.migrate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.plugin.core.ProviderEconomy;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.command.Subcommand;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.debug.DebugCategory;
import me.lokka30.treasury.plugin.core.debug.DebugHandler;
import me.lokka30.treasury.plugin.core.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder.placeholder;

public class MigrateSubcommand implements Subcommand {

    /*
    inf: Migrates accounts from one economy plugin to another
    cmd: /treasury migrate <providerFrom> <providerTo>
    arg:         |       0              1            2
    len:         0       1              2            3
     */

    @Override
    public void execute(@NotNull CommandSource sender, @NotNull String label, @NotNull String[] args) {
        final boolean debugEnabled = DebugHandler.isCategoryEnabled(DebugCategory.MIGRATE_SUBCOMMAND);

        if (!Utils.checkPermissionForCommand(sender, "treasury.command.treasury.migrate")) return;

        List<ProviderEconomy> serviceProviders = TreasuryPlugin.getInstance().allProviders();

        if (args.length != 2) {
            sender.sendMessage(Message.of(
                    MessageKey.MIGRATE_INVALID_USAGE,
                    placeholder("label", label),
                    placeholder(
                            "providers",
                            serviceProviders.isEmpty()
                                    ? "No providers found "
                                    : Utils.formatListMessage(
                                            serviceProviders.stream()
                                                    .map(provider -> provider.registrar().getName())
                                                    .collect(Collectors.toList()))
                    ))
            );
            return;
        }

        ProviderEconomy from = null;
        ProviderEconomy to = null;

        if (serviceProviders.size() < 2) {
            sender.sendMessage(Message.of(MessageKey.MIGRATE_REQUIRES_TWO_PROVIDERS));
            return;
        }

        final Set<String> serviceProvidersNames = new HashSet<>();

        for (ProviderEconomy serviceProvider : serviceProviders) {
            serviceProvidersNames.add(serviceProvider.registrar().getName());
            if (debugEnabled) {
                DebugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Found service provider: " + serviceProvider.registrar().getName());
            }
        }

        if (args[0].equalsIgnoreCase(args[1])) {
            sender.sendMessage(Message.of(
                    MessageKey.MIGRATE_PROVIDERS_MATCH,
                    placeholder("providers", Utils.formatListMessage(serviceProvidersNames)))
            );
            return;
        }

        for (ProviderEconomy serviceProvider : serviceProviders) {
            final String serviceProviderPluginName = serviceProvider.registrar().getName();

            if (args[0].equalsIgnoreCase(serviceProviderPluginName)) {
                from = serviceProvider;
            } else if (args[1].equalsIgnoreCase(serviceProviderPluginName)) {
                to = serviceProvider;
            }
        }

        if (from == null) {
            sender.sendMessage(Message.of(
                    MessageKey.MIGRATE_REQUIRES_VALID_FROM,
                    placeholder("providers", Utils.formatListMessage(serviceProvidersNames)))
            );
            return;
        }

        if (to == null) {
            sender.sendMessage(Message.of(
                    MessageKey.MIGRATE_REQUIRES_VALID_TO,
                    placeholder("providers", Utils.formatListMessage(serviceProvidersNames)))
            );
            return;
        }

        if (debugEnabled) {
            DebugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, "Migrating from '&b" + from.registrar().getName() + "&7' to '&b" + to.registrar().getName() + "&7'.");
        }

        sender.sendMessage(Message.of(MessageKey.MIGRATE_STARTING_MIGRATION));

        // Override economies with dummy economy that doesn't support any operations.
        MigrationEconomy dummyEconomy = new MigrationEconomy();
        TreasuryPlugin.getInstance().registerProvider(dummyEconomy);

        // Re-register economies to ensure target economy will override migrated economy.
        TreasuryPlugin.getInstance().reregisterProvider(from, true);
        TreasuryPlugin.getInstance().reregisterProvider(to, false);

        MigrationData migration = new MigrationData(from, to, debugEnabled);

        TreasuryPlugin.getInstance().scheduler().runAsync(() -> {

            // Block until currencies have been populated.
            establishCurrencies(migration).arriveAndAwaitAdvance();

            if (migration.migratedCurrencies().isEmpty()) {
                // Nothing to migrate. Maybe a special message?
                sendMigrationMessage(sender, migration);
                return;
            }

            // Initialize account migration.
            Phaser playerMigration = migrateAccounts(migration, new PlayerAccountMigrator());
            EconomyProvider fromProvider = migration.from().provide();
            EconomyProvider toProvider = migration.to().provide();
            if (fromProvider.hasBankAccountSupport()) {
                if (toProvider.hasBankAccountSupport()) {
                    Phaser bankMigration = migrateAccounts(migration, new BankAccountMigrator());
                    bankMigration.arriveAndAwaitAdvance();
                } else {
                    migration.debug(() -> "'&b" + migration.to().registrar().getName() + "&7' does not offer bank support, cannot transfer accounts.");
                }
            }

            // Block until migration is complete.
            playerMigration.arriveAndAwaitAdvance();

            // Unregister economy override.
            TreasuryPlugin.getInstance().unregisterProvider(dummyEconomy);

            sendMigrationMessage(sender, migration);
        });
    }

    @Override
    @Nullable
    public List<String> complete(@NotNull CommandSource source, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return Collections.emptyList();
        }
        if ((args.length == 1 || args.length == 2) && source.hasPermission("treasury.command.treasury.migrate")) {
            String lastArg = args[args.length - 1].toLowerCase(Locale.ROOT);
            return TreasuryPlugin.getInstance().pluginsListRegisteringProvider()
                    .stream()
                    .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(lastArg))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void sendMigrationMessage(@NotNull CommandSource sender, @NotNull MigrationData migration) {
        sender.sendMessage(Message.of(
                MessageKey.MIGRATE_FINISHED_MIGRATION,
                placeholder("time", migration.timer().getTimer()),
                placeholder("player-accounts", migration.playerAccountsProcessed().toString()),
                placeholder("bank-accounts", migration.bankAccountsProcessed().toString()),
                placeholder("migrated-currencies", Utils.formatListMessage(migration.migratedCurrencies().keySet().stream().map(Currency::getCurrencyName).collect(Collectors.toList()))),
                placeholder("non-migrated-currencies", Utils.formatListMessage(migration.nonMigratedCurrencies())))
        );
    }

    private Phaser establishCurrencies(@NotNull MigrationData migration) {
        CompletableFuture<Collection<UUID>> fromCurrencyIdsFuture = new CompletableFuture<>();

        // Initialize phaser with a single party: currency mapping completion.
        Phaser phaser = new Phaser(1);

        migration.from().provide().retrieveCurrencyIds(new PhasedFutureSubscriber<>(phaser, fromCurrencyIdsFuture));

        fromCurrencyIdsFuture.thenAccept(fromCurrencyIds -> {
            for (UUID fromCurrencyId : fromCurrencyIds) {

                // Fetch from currency.
                CompletableFuture<Currency> fromCurrencyFuture = new CompletableFuture<>();
                migration.from().provide().retrieveCurrency(fromCurrencyId, new PhasedFutureSubscriber<>(phaser, fromCurrencyFuture));
                fromCurrencyFuture.whenComplete(((currency, throwable) -> {
                    if (throwable != null) {
                        migration.debug(() -> "Unable to locate reported currency with ID '&b" + fromCurrencyId + "&7'.");
                    }
                }));

                fromCurrencyFuture.thenAccept(fromCurrency -> {

                    // Fetch to currency.
                    CompletableFuture<Currency> toCurrencyFuture = new CompletableFuture<>();
                    migration.to().provide().retrieveCurrency(fromCurrencyId, new PhasedFutureSubscriber<>(phaser, toCurrencyFuture));
                    toCurrencyFuture.whenComplete(((toCurrency, throwable) -> {
                        if (toCurrency == null) {
                            // Currency not found.
                            migration.nonMigratedCurrencies().add(fromCurrency.getCurrencyName());
                            migration.debug(() -> "Currency of ID '&b" + fromCurrency.getCurrencyName() + "&7' will not be migrated.");
                        } else {
                            // Currency located, map.
                            migration.migratedCurrencies().put(fromCurrency, toCurrency);
                            migration.debug(() -> "Currency of ID '&b" + fromCurrency.getCurrencyName() + "&7' will be migrated.");
                        }
                    }));
                });
            }
            phaser.arriveAndDeregister();
        });

        return phaser;
    }

    private <T extends Account> Phaser migrateAccounts(@NotNull MigrationData migration, @NotNull AccountMigrator<T> migrator) {
        // Initialize phaser with a single party: migration completion.
        Phaser phaser = new Phaser(1);

        migrator.requestAccountIds().accept(migration.from().provide(), new PhasedSubscriber<Collection<UUID>>(phaser) {
            @Override
            public void phaseAccept(@NotNull Collection<UUID> uuids) {
                for (UUID uuid : uuids) {
                    migrateAccount(phaser, uuid, migration, migrator);
                }
            }

            @Override
            public void phaseFail(@NotNull EconomyException exception) {
                migration.debug(() -> migrator.getBulkFailLog(exception));
            }
        });

        return phaser;
    }

    private <T extends Account> void migrateAccount(
            @NotNull Phaser phaser,
            @NotNull UUID uuid,
            @NotNull MigrationData migration,
            @NotNull AccountMigrator<T> migrator) {
        migration.debug(() -> migrator.getInitLog(uuid));

        // Set up logging for failure.
        // Because from and to accounts are requested in parallel, guard against duplicate failure logging.
        AtomicBoolean failed = new AtomicBoolean();
        BiConsumer<T, Throwable> failureConsumer = (account, throwable) -> {
            if (throwable != null && failed.compareAndSet(false, true)) {
                migration.debug(() -> migrator.getErrorLog(uuid, throwable));
            }
        };

        CompletableFuture<T> fromAccountFuture = new CompletableFuture<>();
        migrator.requestAccount().accept(migration.from().provide(), uuid, new PhasedFutureSubscriber<>(phaser, fromAccountFuture));
        fromAccountFuture.whenComplete(failureConsumer);

        CompletableFuture<T> toAccountFuture = new CompletableFuture<>();
        migrator.checkAccountExistence().accept(migration.to().provide(), uuid, new PhasedSubscriber<Boolean>(phaser) {
            @Override
            public void phaseAccept(@NotNull Boolean hasAccount) {
                PhasedFutureSubscriber<T> subscription = new PhasedFutureSubscriber<>(phaser, toAccountFuture);
                if (hasAccount) {
                    migrator.requestAccount().accept(migration.to().provide(), uuid, subscription);
                } else {
                    migrator.createAccount().accept(migration.to().provide(), uuid, subscription);
                }
            }

            @Override
            public void phaseFail(@NotNull EconomyException exception) {
                toAccountFuture.completeExceptionally(exception);
            }
        });
        toAccountFuture.whenComplete(failureConsumer);

        fromAccountFuture.thenAcceptBoth(toAccountFuture, (fromAccount, toAccount) -> {
            migrator.migrate(phaser, fromAccount, toAccount, migration);
            migrator.getSuccessfulMigrations(migration).incrementAndGet();
        });
    }

}
