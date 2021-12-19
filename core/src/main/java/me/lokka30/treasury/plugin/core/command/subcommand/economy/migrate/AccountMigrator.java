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
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.jetbrains.annotations.NotNull;

interface AccountMigrator<T extends Account> {

    @NotNull String getBulkFailLog(@NotNull Throwable throwable);

    @NotNull String getInitLog(@NotNull UUID uuid);

    @NotNull String getErrorLog(@NotNull UUID uuid, @NotNull Throwable throwable);

    @NotNull BiConsumer<@NotNull EconomyProvider, @NotNull EconomySubscriber<Collection<UUID>>> requestAccountIds();

    @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<T>> requestAccount();

    @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<Boolean>> checkAccountExistence();

    @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<T>> createAccount();

    default void migrate(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Phaser phaser,
            @NotNull T fromAccount,
            @NotNull T toAccount,
            @NotNull MigrationData migration
    ) {
        CompletableFuture<Collection<String>> fromCurrencies = new CompletableFuture<>();

        fromAccount.retrieveHeldCurrencies(new PhasedSubscriber<Collection<String>>(phaser) {
            @Override
            public void phaseAccept(@NotNull final Collection<String> currencies) {
                fromCurrencies.complete(currencies);
            }

            @Override
            public void phaseFail(@NotNull final EconomyException exception) {
                migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception));
                fromCurrencies.completeExceptionally(exception);
            }
        });

        fromCurrencies.thenAccept(currenciesIDS -> {

            Collection<Currency> currencies = currenciesIDS.stream().map(identifier->{
                if(TreasuryPlugin.getInstance().economyProviderProvider() == null) {
                    migration.debug(() -> "Economy provider is null.");
                    return null;
                }

                Optional<Currency> currency =
                        TreasuryPlugin.getInstance().economyProviderProvider().provide().findCurrency(identifier);

                if(currency.isPresent()) {
                    return currency.get();
                } else {
                    migration.debug(() -> "Currency with ID '&b" + identifier + "&7' will " + "not be migrated.");
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
                        fromAccount.setBalance(balance, initiator, currency, new FailureConsumer<>(phaser, exception1 -> {
                            migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception1));
                            migration.debug(() -> String.format(
                                    "Failed to recover from an issue transferring %s %s from %s, currency will not be migrated!",
                                    balance,
                                    currency.displayName(),
                                    fromAccount.getUniqueId()
                            ));
                            if (!migration.nonMigratedCurrencies().contains(currency.displayName())) {
                                migration.nonMigratedCurrencies().add(currency.displayName());
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
