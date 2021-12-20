/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.currency.CurrencyManager;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;

interface AccountMigrator<T extends Account> {

    @NotNull String getBulkFailLog(@NotNull Throwable throwable);

    @NotNull String getInitLog(@NotNull String identifier);

    @NotNull String getErrorLog(@NotNull String identifier, @NotNull Throwable throwable);

    @NotNull BiConsumer<@NotNull EconomyProvider, @NotNull EconomySubscriber<Collection<String>>> requestAccountIds();

    @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull String, @NotNull EconomySubscriber<T>> requestAccount();

    @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull String, @NotNull EconomySubscriber<Boolean>> checkAccountExistence();

    @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull String, @NotNull EconomySubscriber<T>> createAccount();

    default void migrate(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Phaser phaser,
            @NotNull T fromAccount,
            @NotNull T toAccount,
            @NotNull MigrationData migration
    ) {
        CompletableFuture<Collection<UUID>> fromCurrencies = new CompletableFuture<>();

        fromAccount.retrieveHeldCurrencies(new PhasedSubscriber<Collection<UUID>>(phaser) {
            @Override
            public void phaseAccept(@NotNull final Collection<UUID> uuids) {
                fromCurrencies.complete(uuids);
            }

            @Override
            public void phaseFail(@NotNull final EconomyException exception) {
                migration.debug(() -> getErrorLog(fromAccount.getIdentifier(), exception));
                fromCurrencies.completeExceptionally(exception);
            }
        });

        fromCurrencies.thenAccept(currencyIds -> {
            Collection<Currency> currencies = currencyIds.stream().map(uuid -> {
                Optional<Currency> currency = CurrencyManager.INSTANCE.getCurrency(uuid);
                if (currency.isPresent()) {
                    return currency.get();
                } else {
                    migration.debug(() -> "Currency with ID '&b" + uuid + "&7' will " + "not be migrated.");
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());

            for (Currency currency : currencies) {
                CompletableFuture<Double> balanceFuture = new CompletableFuture<>();

                fromAccount.setBalance(0.0D, initiator, currency, new PhasedSubscriber<Double>(phaser) {
                    @Override
                    public void phaseAccept(@NotNull final Double balance) {
                        balanceFuture.complete(balance);
                    }

                    @Override
                    public void phaseFail(@NotNull final EconomyException exception) {
                        migration.debug(() -> getErrorLog(fromAccount.getIdentifier(), exception));
                        balanceFuture.completeExceptionally(exception);
                    }
                });

                balanceFuture.thenAccept(balance -> {
                    if (balance == 0) {
                        return;
                    }

                    EconomySubscriber<Double> subscriber = new FailureConsumer<>(phaser, exception -> {
                        migration.debug(() -> getErrorLog(fromAccount.getIdentifier(), exception));
                        fromAccount.setBalance(balance, initiator, currency, new FailureConsumer<>(phaser, exception1 -> {
                            migration.debug(() -> getErrorLog(fromAccount.getIdentifier(), exception1));
                            migration.debug(() -> String.format(
                                    "Failed to recover from an issue transferring %s %s from %s, currency will not be migrated!",
                                    balance,
                                    currency.getPrimaryCurrencyName(),
                                    fromAccount.getIdentifier()
                            ));
                            if (!migration.nonMigratedCurrencies().contains(currency.getPrimaryCurrencyName())) {
                                migration.nonMigratedCurrencies().add(currency.getPrimaryCurrencyName());
                            }
                        }));
                    });

                    if (balance < 0) {
                        toAccount.withdrawBalance(balance, initiator, currency, subscriber);
                    } else {
                        toAccount.depositBalance(balance, initiator, currency, subscriber);
                    }
                });
            }
        });
    }

    @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration);

}