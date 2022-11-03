/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionImportance;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import org.jetbrains.annotations.NotNull;

interface AccountMigrator<T extends Account> {

    @NotNull String getBulkFailLog(@NotNull FailureReason failureReason);

    @NotNull String getInitLog(@NotNull String identifier);

    @NotNull String getErrorLog(@NotNull String identifier, @NotNull FailureReason failureReason);

    @NotNull CompletableFuture<Response<Collection<String>>> requestAccountIds(@NotNull EconomyProvider provider);

    @NotNull CompletableFuture<Response<T>> requestOrCreateAccount(@NotNull EconomyProvider provider, String identifier);

    @NotNull
    default CountDownLatch migrate(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull T fromAccount,
            @NotNull T toAccount,
            @NotNull MigrationData migration
    ) {
        // todo: a proper way is needed to wait for this method to finish before proceeding
        // using .join or .get on futures is not the proper solution and i'd like to avoid it
        // the latch currently implemented captures whenever fromAccount.resetBalance future
        // finishes and NOT when .thenAccept finishes which is a problem
        CountDownLatch latch = new CountDownLatch(1);
        fromAccount.retrieveHeldCurrencies().thenAccept(resp -> {
            if (!resp.isSuccessful()) {
                migration.debug(() -> getErrorLog(
                        fromAccount.getIdentifier(),
                        resp.getFailureReason()
                ));
                return;
            }

            List<CompletableFuture<?>> futures = new ArrayList<>();
            for (String id : resp.getResult()) {
                if (ServiceRegistry.INSTANCE.hasRegistration(EconomyProvider.class)) {
                    migration.debug(() -> "Economy provider is null.");
                    continue;
                }

                Optional<Currency> currency = ServiceRegistry.INSTANCE
                        .serviceFor(EconomyProvider.class)
                        .get()
                        .get()
                        .findCurrency(id);

                if (currency.isPresent()) {
                    futures.add(fromAccount.resetBalance(
                            initiator,
                            currency.get(),
                            EconomyTransactionImportance.NORMAL,
                            "normal"
                    ).thenAccept(balResp -> {
                        if (!balResp.isSuccessful()) {
                            migration.debug(() -> getErrorLog(
                                    fromAccount.getIdentifier(),
                                    balResp.getFailureReason()
                            ));
                            return;
                        }

                        BigDecimal balance = balResp.getResult();
                        if (balance.compareTo(BigDecimal.ZERO) == 0) {
                            return;
                        }

                        Consumer<Response<BigDecimal>> action = (resp1) -> {
                            if (!resp1.isSuccessful()) {
                                migration.debug(() -> getErrorLog(
                                        fromAccount.getIdentifier(),
                                        resp1.getFailureReason()
                                ));
                                return;
                            }

                            fromAccount.doTransaction(
                                    EconomyTransaction
                                            .newBuilder()
                                            .withTransactionType(EconomyTransactionType.SET)
                                            .withImportance(EconomyTransactionImportance.NORMAL)
                                            .withReason("migration")
                                            .withTransactionAmount(balance)
                                            .withInitiator(initiator)
                                            .withCurrency(currency.get())
                                            .build()
                            ).thenAccept((transResp) -> {
                                if (!transResp.isSuccessful()) {
                                    migration.debug(() -> getErrorLog(
                                            fromAccount.getIdentifier(),
                                            transResp.getFailureReason()
                                    ));
                                    migration.debug(() -> String.format(
                                            "Failed to recover from an issue transferring %s %s from %s, currency will not be migrated!",
                                            balance,
                                            currency.get().getDisplayNameSingular(),
                                            fromAccount.getIdentifier()
                                    ));
                                    if (!migration.nonMigratedCurrencies().contains(currency
                                            .get()
                                            .getIdentifier())) {
                                        migration.nonMigratedCurrencies().add(currency
                                                .get()
                                                .getIdentifier());
                                    }
                                }
                            });
                        };

                        if (balance.compareTo(BigDecimal.ZERO) < 0) {
                            toAccount
                                    .withdrawBalance(balance, initiator, currency.get())
                                    .thenAccept(action);
                        } else {
                            toAccount
                                    .depositBalance(balance, initiator, currency.get())
                                    .thenAccept(action);
                        }
                    }));
                } else {
                    futures.add(CompletableFuture.completedFuture(null));
                    migration.debug(() -> "Currency with ID '&b" + id + "&7' will not be migrated.");
                }
            }
            CompletableFuture
                    .allOf(futures.toArray(new CompletableFuture[0]))
                    .thenRun(latch::countDown);
        });

        return latch;
    }

    @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration);

}
