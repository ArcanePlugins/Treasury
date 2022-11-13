/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;

class NonPlayerAccountMigrator implements AccountMigrator<Account> {

    @Override
    public @NotNull String getBulkFailLog(@NotNull Throwable throwable) {
        return "Unable to fetch non player account UUIDs for migration: " + throwable.getMessage();
    }

    @Override
    public @NotNull String getInitLog(@NotNull String identifier) {
        return "Migrating non player account of ID '&b" + identifier + "&7'.";
    }

    @Override
    public @NotNull String getErrorLog(@NotNull String identifier, @NotNull Throwable throwable) {
        return "Error migrating non player account ID '&b" + identifier + "&7': &b" + throwable.getMessage();
    }

    @Override
    public @NotNull BiConsumer<@NotNull EconomyProvider, @NotNull EconomySubscriber<Collection<String>>> requestAccountIds() {
        return EconomyProvider::retrieveNonPlayerAccountIds;
    }

    @Override
    public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull String, @NotNull EconomySubscriber<Account>> requestAccount() {
        return EconomyProvider::retrieveAccount;
    }

    @Override
    public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull String, @NotNull EconomySubscriber<Boolean>> checkAccountExistence() {
        return EconomyProvider::hasAccount;
    }

    @Override
    public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull String, @NotNull EconomySubscriber<Account>> createAccount() {
        return EconomyProvider::createAccount;
    }

    @Override
    public void migrate(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Phaser phaser,
            @NotNull Account fromAccount,
            @NotNull Account toAccount,
            @NotNull MigrationData migration
    ) {
        AccountMigrator.super.migrate(initiator, phaser, fromAccount, toAccount, migration);

        CompletableFuture<Collection<UUID>> memberUuidsFuture = new CompletableFuture<>();
        fromAccount.retrieveMemberIds(new PhasedFutureSubscriber<>(phaser, memberUuidsFuture));
        memberUuidsFuture.thenAccept(uuids -> {
            for (UUID uuid : uuids) {
                fromAccount.retrievePermissions(uuid,
                        new EconomySubscriber<Map<AccountPermission, TriState>>() {
                            @Override
                            public void succeed(@NotNull final Map<AccountPermission, TriState> map) {
                                for (Map.Entry<AccountPermission, TriState> entry : map.entrySet()) {
                                    toAccount.setPermission(uuid,
                                            entry.getValue(),
                                            new FailureConsumer<>(phaser,
                                                    exception -> migration.debug(() -> getErrorLog(
                                                            fromAccount.getIdentifier(),
                                                            exception
                                                    ))
                                            ),
                                            entry.getKey()
                                    );
                                }
                            }

                            @Override
                            public void fail(@NotNull final EconomyException exception) {
                                migration.debug(() -> getErrorLog(fromAccount.getIdentifier(),
                                        exception
                                ));
                            }
                        }
                );
            }
        });
    }

    @Override
    public @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration) {
        return migration.nonPlayerAccountsProcessed();
    }

}
