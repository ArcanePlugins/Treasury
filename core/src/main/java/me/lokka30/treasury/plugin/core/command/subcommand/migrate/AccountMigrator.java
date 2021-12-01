package me.lokka30.treasury.plugin.core.command.subcommand.migrate;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

interface AccountMigrator<T extends Account> {

    @NotNull String getBulkFailLog(@NotNull Throwable throwable);

    @NotNull String getInitLog(@NotNull UUID uuid);

    @NotNull String getErrorLog(@NotNull UUID uuid, @NotNull Throwable throwable);

    @NotNull BiConsumer<@NotNull EconomyProvider, @NotNull EconomySubscriber<Collection<UUID>>> requestAccountIds();

    @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<T>> requestAccount();

    @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<Boolean>> checkAccountExistence();

    @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<T>> createAccount();

    default void migrate(
            @NotNull Phaser phaser,
            @NotNull T fromAccount,
            @NotNull T toAccount,
            @NotNull MigrationData migration) {
        for (Map.Entry<Currency, Currency> fromToCurrency : migration.migratedCurrencies().entrySet()) {
            Currency fromCurrency = fromToCurrency.getKey();
            Currency toCurrency = fromToCurrency.getValue();
            CompletableFuture<Double> balanceFuture = new CompletableFuture<>();

            fromAccount.setBalance(0.0D, fromCurrency, new PhasedSubscriber<Double>(phaser) {
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
                EconomySubscriber<Double> subscriber = new FailureConsumer<>(phaser, exception -> {
                    migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception));
                    fromAccount.setBalance(balance, fromCurrency, new FailureConsumer<>(phaser, exception1 -> {
                        migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception1));
                        migration.debug(() -> String.format(
                                "Failed to recover from an issue transferring %s %s from %s, currency will be deleted!",
                                balance,
                                fromCurrency.getCurrencyName(),
                                fromAccount.getUniqueId()));
                    }));
                });
                if (balance < 0) {
                    toAccount.withdrawBalance(balance, toCurrency, subscriber);
                } else {
                    toAccount.depositBalance(balance, toCurrency, subscriber);
                }
            });
        }
    }

    @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration);

}
