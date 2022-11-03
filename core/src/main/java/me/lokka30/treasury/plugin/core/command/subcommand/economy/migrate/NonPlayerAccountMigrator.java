/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;

class NonPlayerAccountMigrator implements AccountMigrator<NonPlayerAccount> {

    @Override
    public @NotNull String getBulkFailLog(final @NotNull FailureReason failureReason) {
        return "Unable to fetch non player account UUIDs for migration: " + failureReason.getDescription();
    }

    @Override
    public @NotNull String getInitLog(@NotNull String identifier) {
        return "Migrating non player account of ID '&b" + identifier + "&7'.";
    }

    @Override
    public @NotNull String getErrorLog(
            @NotNull String identifier, @NotNull FailureReason failureReason
    ) {
        return "Error migrating non player account ID '&b" + identifier + "&7': &b" + failureReason.getDescription();
    }

    @Override
    public @NotNull CompletableFuture<Response<Collection<String>>> requestAccountIds(@NotNull final EconomyProvider provider) {
        return provider.retrieveNonPlayerAccountIds();
    }

    @Override
    public @NotNull CompletableFuture<Response<NonPlayerAccount>> requestOrCreateAccount(
            @NotNull final EconomyProvider provider, final String identifier
    ) {
        return provider.accountAccessor().nonPlayer().withIdentifier(identifier).get();
    }

    @Override
    @NotNull
    public CountDownLatch migrate(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull NonPlayerAccount fromAccount,
            @NotNull NonPlayerAccount toAccount,
            @NotNull MigrationData migration
    ) {
        CountDownLatch latch1 = AccountMigrator.super.migrate(initiator,
                fromAccount,
                toAccount,
                migration
        );

        try {
            latch1.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        CountDownLatch latch = new CountDownLatch(1);
        fromAccount.retrieveMemberIds().thenAccept(resp -> {
            if (!resp.isSuccessful()) {
                migration.debug(() -> getErrorLog(fromAccount.getIdentifier(),
                        resp.getFailureReason()
                ));
                return;
            }

            List<CompletableFuture<?>> futures = new ArrayList<>();
            for (UUID uuid : resp.getResult()) {
                futures.add(fromAccount.retrievePermissions(uuid).thenApply(permResp -> {
                    if (!permResp.isSuccessful()) {
                        migration.debug(() -> getErrorLog(fromAccount.getIdentifier(),
                                permResp.getFailureReason()
                        ));
                        return null;
                    }

                    return permResp.getResult();
                }).thenCompose(permissions -> {
                    if (permissions == null) {
                        return CompletableFuture.completedFuture(null);
                    }
                    // optimise setPermission calls
                    List<AccountPermission> truePermissions = null, falsePermissions = null;
                    for (Map.Entry<AccountPermission, TriState> entry : permissions.entrySet()) {
                        if (entry.getValue() == TriState.TRUE) {
                            if (truePermissions == null) {
                                truePermissions = new ArrayList<>();
                            }
                            truePermissions.add(entry.getKey());
                        } else if (entry.getValue() == TriState.FALSE) {
                            if (falsePermissions == null) {
                                falsePermissions = new ArrayList<>();
                            }
                            falsePermissions.add(entry.getKey());
                        }
                    }
                    List<AccountPermission> falsePermissionsFinal = falsePermissions;
                    if (truePermissions != null) {
                        return toAccount.setPermission(uuid,
                                TriState.TRUE,
                                truePermissions.toArray(new AccountPermission[0])
                        ).thenCompose(response -> {
                            if (!response.isSuccessful()) {
                                migration.debug(() -> getErrorLog(fromAccount.getIdentifier(),
                                        response.getFailureReason()
                                ));
                            }
                            return falsePermissionsFinal == null
                                    ? CompletableFuture.completedFuture(null)
                                    : toAccount.setPermission(uuid,
                                            TriState.FALSE,
                                            falsePermissionsFinal.toArray(new AccountPermission[0])
                                    ).thenCompose((resp1) -> {
                                        if (!resp1.isSuccessful()) {
                                            migration.debug(() -> getErrorLog(fromAccount.getIdentifier(),
                                                    response.getFailureReason()
                                            ));
                                        }
                                        return CompletableFuture.completedFuture(null);
                                    });
                        });
                    } else if (falsePermissions != null) {
                        return toAccount.setPermission(uuid,
                                TriState.FALSE,
                                falsePermissions.toArray(new AccountPermission[0])
                        ).thenCompose(response -> {
                            if (!response.isSuccessful()) {
                                migration.debug(() -> getErrorLog(fromAccount.getIdentifier(),
                                        response.getFailureReason()
                                ));
                            }
                            // no need to check whether true permissions is not null, it is null
                            // already
                            return CompletableFuture.completedFuture(null);
                        });
                    } else {
                        return CompletableFuture.completedFuture(null);
                    }
                }));
            }
            CompletableFuture
                    .allOf(futures.toArray(new CompletableFuture[0]))
                    .thenRun(latch::countDown);
        });
        return latch;
    }

    @Override
    public @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration) {
        return migration.nonPlayerAccountsProcessed();
    }

}
