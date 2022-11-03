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
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionImportance;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;

interface AccountMigrator<T extends Account> {

    @NotNull String getBulkFailLog(@NotNull FailureReason failureReason);

    @NotNull String getInitLog(@NotNull String identifier);

    @NotNull String getErrorLog(@NotNull String identifier, @NotNull FailureReason failureReason);

    @NotNull CompletableFuture<Response<Collection<String>>> requestAccountIds(@NotNull EconomyProvider provider);

    @NotNull CompletableFuture<Response<T>> requestOrCreateAccount(
            @NotNull EconomyProvider provider, String identifier
    );

    @NotNull
    default CountDownLatch migrate(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull T fromAccount,
            @NotNull T toAccount,
            @NotNull MigrationData migration
    ) {
        CountDownLatch latch = new CountDownLatch(1);
        String accountId = fromAccount.getIdentifier();
        fromAccount.retrieveHeldCurrencies().thenAccept(resp -> {
            if (!resp.isSuccessful()) {
                migration.debug(() -> getErrorLog(accountId, resp.getFailureReason()));
                return;
            }

            List<CompletableFuture<?>> futures = new ArrayList<>();
            for (String id : resp.getResult()) {
                if (ServiceRegistry.INSTANCE.hasRegistration(EconomyProvider.class)) {
                    migration.debug(() -> "Economy provider is null.");
                    continue;
                }

                Optional<Currency> currencyOpt = ServiceRegistry.INSTANCE
                        .serviceFor(EconomyProvider.class)
                        .get()
                        .get()
                        .findCurrency(id);

                if (currencyOpt.isPresent()) {
                    Currency currency = currencyOpt.get();
                    futures.add(fromAccount.retrieveBalance(currency).thenApply(balResp -> {
                        if (!balResp.isSuccessful()) {
                            migration.debug(() -> getErrorLog(accountId,
                                    balResp.getFailureReason()
                            ));
                            return null;
                        }

                        return balResp.getResult();
                    }).thenCompose(bal -> {
                        if (bal == null) {
                            return CompletableFuture.completedFuture(null);
                        }
                        if (bal.compareTo(BigDecimal.ZERO) == 0) {
                            return CompletableFuture.completedFuture(null);
                        }

                        return fromAccount.resetBalance(initiator,
                                currency,
                                EconomyTransactionImportance.NORMAL,
                                "migration"
                        ).thenApply(newRes -> {
                            if (!newRes.isSuccessful()) {
                                migration.debug(() -> getErrorLog(accountId,
                                        newRes.getFailureReason()
                                ));
                                return bal;
                            }
                            return bal;
                        });
                    }).thenCompose(balance -> {
                        if (balance == null) {
                            return CompletableFuture.completedFuture(null);
                        }
                        return balance.compareTo(BigDecimal.ZERO) < 0 ? toAccount.withdrawBalance(balance,
                                initiator,
                                currency
                        ) : toAccount.depositBalance(balance, initiator, currency);
                    }).thenApply(depResult -> {
                        if (depResult == null || !depResult.isSuccessful()) {
                            migration.debug(() -> getErrorLog(accountId,
                                    depResult.getFailureReason()
                            ));
                            migration.debug(() -> String.format(
                                    "Failed to recover from an issue transferring %s from %s, currency will not be migrated!",
                                    currency.getDisplayNameSingular(),
                                    fromAccount.getIdentifier()
                            ));
                            Collection<String> currencies = migration.nonMigratedCurrencies().get(
                                    accountId);
                            if (!currencies.contains(currency.getIdentifier())) {
                                migration.nonMigratedCurrencies().put(accountId,
                                        currency.getIdentifier()
                                );
                            }
                        }
                        return null;
                    }));
                } else {
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
