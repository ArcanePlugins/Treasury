/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.Collection;
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
        // todo: see AccountMigrator#migrate
        AccountMigrator.super.migrate(initiator, fromAccount, toAccount, migration);

        fromAccount.retrieveMemberIds().thenAccept(resp -> {
            if (!resp.isSuccessful()) {
                migration.debug(() -> getErrorLog(
                        fromAccount.getIdentifier(),
                        resp.getFailureReason()
                ));
                return;
            }

            for (UUID uuid : resp.getResult()) {
                fromAccount.retrievePermissions(uuid).thenAccept((permResp) -> {
                    if (!permResp.isSuccessful()) {
                        migration.debug(() -> getErrorLog(
                                fromAccount.getIdentifier(),
                                permResp.getFailureReason()
                        ));
                        return;
                    }

                    for (Map.Entry<AccountPermission, TriState> entry : permResp
                            .getResult()
                            .entrySet()) {
                        toAccount
                                .setPermission(uuid, entry.getValue(), entry.getKey())
                                .thenAccept((resp1) -> {
                                    if (!resp1.isSuccessful()) {
                                        migration.debug(() -> getErrorLog(
                                                fromAccount.getIdentifier(),
                                                resp1.getFailureReason()
                                        ));
                                    }
                                });
                    }
                });
            }
        });
        return null; // todo
    }

    @Override
    public @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration) {
        return migration.nonPlayerAccountsProcessed();
    }

}
