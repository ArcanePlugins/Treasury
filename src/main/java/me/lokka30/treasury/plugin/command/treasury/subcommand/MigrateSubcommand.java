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
import me.lokka30.treasury.api.economy.account.BankAccount;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

        MigrationData migration = new MigrationData(main, from, to, debugEnabled);

        main.getServer().getScheduler().runTaskAsynchronously(main, () -> {

            // Block until currencies have been populated.
            establishCurrencies(migration).arriveAndAwaitAdvance();

            if (migration.migratedCurrencies.isEmpty()) {
                // Nothing to migrate. Maybe a special message?
                sendMigrationMessage(sender, migration);
                return;
            }

            // Initialize account migration.
            Phaser playerMigration = migrateAccounts(migration, new PlayerAccountMigrator());
            if (migration.from.hasBankAccountSupport()) {
                if (migration.to.hasBankAccountSupport()) {
                    Phaser bankMigration = migrateAccounts(migration, new BankAccountMigrator());
                    bankMigration.arriveAndAwaitAdvance();
                } else {
                    migration.debug(() -> "'&b" + migration.to.getProvider().getName() + "&7' does not offer bank support, cannot transfer accounts.");
                }
            }

            // Block until migration is complete.
            playerMigration.arriveAndAwaitAdvance();

            sendMigrationMessage(sender, migration);
        });
    }

    private void sendMigrationMessage(@NotNull CommandSender sender, @NotNull MigrationData migration) {
        new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.migrate.finished-migration"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                new MultiMessage.Placeholder("time", migration.timer.getTimer() + "", false),
                new MultiMessage.Placeholder("player-accounts", migration.playerAccountsProcessed.toString(), false),
                new MultiMessage.Placeholder("bank-accounts", migration.bankAccountsProcessed.toString(), false),
                new MultiMessage.Placeholder("migrated-currencies", Utils.formatListMessage(main, migration.migratedCurrencies.keySet().stream().map(Currency::getCurrencyName).collect(Collectors.toList())), false),
                new MultiMessage.Placeholder("non-migrated-currencies", Utils.formatListMessage(main, migration.nonMigratedCurrencies), false)
        )).send(sender);
    }

    private Phaser establishCurrencies(@NotNull MigrationData migration) {
        CompletableFuture<Collection<UUID>> fromCurrencyIdsFuture = new CompletableFuture<>();

        // Initialize phaser with a single party: currency mapping completion.
        Phaser phaser = new Phaser(1);

        migration.from.requestCurrencyIds(new PhasedFutureSubscriber<>(phaser, fromCurrencyIdsFuture));

        fromCurrencyIdsFuture.thenAccept(fromCurrencyIds -> {
            for (UUID fromCurrencyId : fromCurrencyIds) {

                // Fetch from currency.
                CompletableFuture<Currency> fromCurrencyFuture = new CompletableFuture<>();
                migration.from.requestCurrency(fromCurrencyId, new PhasedFutureSubscriber<>(phaser, fromCurrencyFuture));
                fromCurrencyFuture.whenComplete(((currency, throwable) -> {
                    if (throwable != null) {
                        migration.debug(() -> "Unable to locate reported currency with ID '&b" + fromCurrencyId + "&7'.");
                    }
                }));

                fromCurrencyFuture.thenAccept(fromCurrency -> {

                    // Fetch to currency.
                    CompletableFuture<Currency> toCurrencyFuture = new CompletableFuture<>();
                    migration.to.requestCurrency(fromCurrencyId, new PhasedFutureSubscriber<>(phaser, toCurrencyFuture));
                    toCurrencyFuture.whenComplete(((toCurrency, throwable) -> {
                        if (toCurrency == null) {
                            // Currency not found.
                            migration.nonMigratedCurrencies.add(fromCurrency.getCurrencyName());
                            migration.debug(() -> "Currency of ID '&b" + fromCurrency.getCurrencyName() + "&7' will not be migrated.");
                        } else {
                            // Currency located, map.
                            migration.migratedCurrencies.put(fromCurrency, toCurrency);
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

        migration.from.requestPlayerAccountIds(new PhasedSubscriber<Collection<UUID>>(phaser) {
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
        migrator.requestAccount().accept(migration.from, uuid, new PhasedFutureSubscriber<>(phaser, fromAccountFuture));
        fromAccountFuture.whenComplete(failureConsumer);

        CompletableFuture<T> toAccountFuture = new CompletableFuture<>();
        migrator.checkAccountExistence().accept(migration.to, uuid, new PhasedSubscriber<Boolean>(phaser) {
            @Override
            public void phaseAccept(@NotNull Boolean hasAccount) {
                PhasedFutureSubscriber<T> subscription = new PhasedFutureSubscriber<>(phaser, toAccountFuture);
                if (hasAccount) {
                    migrator.requestAccount().accept(migration.to, uuid, subscription);
                } else {
                    migrator.createAccount().accept(migration.to, uuid, subscription);
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

    private static class MigrationData {
        private final @NotNull Treasury treasury;
        private final @NotNull EconomyProvider from;
        private final @NotNull EconomyProvider to;
        private final boolean debugEnabled;
        private final QuickTimer timer = new QuickTimer();
        private final @NotNull Map<Currency, Currency> migratedCurrencies = new ConcurrentHashMap<>();
        private final @NotNull Collection<String> nonMigratedCurrencies = new ConcurrentLinkedQueue<>();
        private final @NotNull AtomicInteger playerAccountsProcessed = new AtomicInteger();
        private final @NotNull AtomicInteger bankAccountsProcessed = new AtomicInteger();

        private MigrationData(
                @NotNull Treasury treasury,
                @NotNull EconomyProvider from,
                @NotNull EconomyProvider to,
                boolean debugEnabled) {
            this.treasury = treasury;
            this.from = from;
            this.to = to;
            this.debugEnabled = debugEnabled;
        }

        private void debug(Supplier<String> supplier) {
            if (debugEnabled) {
                treasury.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, supplier.get());
            }
        }
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
            phaseFail(exception);
            phaser.arriveAndDeregister();
        }

        public abstract void phaseFail(@NotNull EconomyException exception);

    }

    private static class PhasedFutureSubscriber<T> extends PhasedSubscriber<T> {
        private final @NotNull CompletableFuture<T> future;

        private PhasedFutureSubscriber(@NotNull Phaser phaser, @NotNull CompletableFuture<T> future) {
            super(phaser);
            this.future = future;
        }

        @Override
        public void phaseAccept(@NotNull T t) {
            future.complete(t);
        }

        @Override
        public void phaseFail(@NotNull EconomyException exception) {
            future.completeExceptionally(exception);
        }
    }

    private interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }

    private interface AccountMigrator<T extends Account> {

        @NotNull String getBulkFailLog(@NotNull Throwable throwable);

        @NotNull String getInitLog(@NotNull UUID uuid);

        @NotNull String getErrorLog(@NotNull UUID uuid, @NotNull Throwable throwable);

        @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<T>> requestAccount();

        @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<Boolean>> checkAccountExistence();

        @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<T>> createAccount();

        default void migrate(
                @NotNull Phaser phaser,
                @NotNull T fromAccount,
                @NotNull T toAccount,
                @NotNull MigrationData migration) {
            for (Map.Entry<Currency, Currency> fromToCurrency : migration.migratedCurrencies.entrySet()) {
                Currency fromCurrency = fromToCurrency.getKey();
                Currency toCurrency = fromToCurrency.getValue();
                CompletableFuture<Double> balanceFuture = new CompletableFuture<>();

                fromAccount.requestBalance(fromCurrency, new PhasedSubscriber<Double>(phaser) {
                    @Override
                    public void phaseAccept(@NotNull Double balance) {
                        balanceFuture.complete(balance);
                    }

                    @Override
                    public void phaseFail(@NotNull EconomyException exception) {
                        migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception));
                        balanceFuture.completeExceptionally(exception);
                    }
                });

                balanceFuture.thenAccept(balance -> {
                    if (balance == 0) {
                        return;
                    }
                    EconomySubscriber<Double> subscriber = new FailureConsumer<>(phaser,
                            exception -> migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception)));
                    if (balance < 0) {
                        toAccount.withdrawBalance(balance, toCurrency, subscriber);
                    } else {
                        toAccount.depositBalance(balance, toCurrency, subscriber);
                    }
                });
            }
        }

        @NotNull AtomicInteger getSuccessfulMigrations(MigrationData migration);

    }

    private static class PlayerAccountMigrator implements AccountMigrator<PlayerAccount> {
        @Override
        public @NotNull String getBulkFailLog(@NotNull Throwable throwable) {
            return "Unable to fetch player account UUIDs for migration: " + throwable.getMessage();
        }

        @Override
        public @NotNull String getInitLog(@NotNull UUID uuid) {
            return "Migrating player account of UUID '&b" + uuid + "&7'.";
        }

        @Override
        public @NotNull String getErrorLog(@NotNull UUID uuid, @NotNull Throwable throwable) {
            return "Error migrating account of player UUID '&b" + uuid + "&7': &b" + throwable.getMessage();
        }

        @Override
        public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<PlayerAccount>> requestAccount() {
            return EconomyProvider::requestPlayerAccount;
        }

        @Override
        public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<Boolean>> checkAccountExistence() {
            return EconomyProvider::hasPlayerAccount;
        }

        @Override
        public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<PlayerAccount>> createAccount() {
            return EconomyProvider::createPlayerAccount;
        }

        @Override
        public @NotNull AtomicInteger getSuccessfulMigrations(MigrationData migration) {
            return migration.playerAccountsProcessed;
        }
    }

    private static class BankAccountMigrator implements AccountMigrator<BankAccount> {

        @Override
        public @NotNull String getBulkFailLog(@NotNull Throwable throwable) {
            return "Unable to fetch bank account UUIDs for migration: " + throwable.getMessage();
        }

        @Override
        public @NotNull String getInitLog(@NotNull UUID uuid) {
            return "Migrating bank account of UUID '&b" + uuid + "&7'.";
        }

        @Override
        public @NotNull String getErrorLog(@NotNull UUID uuid, @NotNull Throwable throwable) {
            return "Error migrating bank account UUID '&b" + uuid + "&7': &b" + throwable.getMessage();
        }

        @Override
        public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<BankAccount>> requestAccount() {
            return EconomyProvider::requestBankAccount;
        }

        @Override
        public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<Boolean>> checkAccountExistence() {
            return EconomyProvider::hasBankAccount;
        }

        @Override
        public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<BankAccount>> createAccount() {
            return EconomyProvider::createBankAccount;
        }

        @Override
        public void migrate(
                @NotNull Phaser phaser,
                @NotNull BankAccount fromAccount,
                @NotNull BankAccount toAccount,
                @NotNull MigrationData migration) {
            AccountMigrator.super.migrate(phaser, fromAccount, toAccount, migration);

            CompletableFuture<Collection<UUID>> memberUuidsFuture = new CompletableFuture<>();
            fromAccount.requestBankMembersIds(new PhasedFutureSubscriber<>(phaser, memberUuidsFuture));
            memberUuidsFuture.thenAccept(uuids -> uuids.forEach(uuid -> toAccount.addBankMember(uuid,
                    new FailureConsumer<>(phaser, exception -> migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception))))));

            CompletableFuture<Collection<UUID>> ownerUuidsFuture = new CompletableFuture<>();
            fromAccount.requestBankOwnersIds(new PhasedFutureSubscriber<>(phaser, ownerUuidsFuture));
            ownerUuidsFuture.thenAccept(uuids -> uuids.forEach(uuid -> toAccount.addBankOwner(uuid,
                    new FailureConsumer<>(phaser, exception -> migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception))))));
        }

        @Override
        public @NotNull AtomicInteger getSuccessfulMigrations(MigrationData migration) {
            return migration.bankAccountsProcessed;
        }
    }

    private static final class FailureConsumer<T> extends PhasedSubscriber<T> {
        private final Consumer<EconomyException> consumer;

        private FailureConsumer(@NotNull Phaser phaser, @NotNull Consumer<EconomyException> consumer) {
            super(phaser);
            this.consumer = consumer;
        }

        @Override
        public void phaseAccept(@NotNull T t) {
            // Do nothing.
        }

        @Override
        public void phaseFail(@NotNull EconomyException exception) {
            consumer.accept(exception);
        }
    }

}
