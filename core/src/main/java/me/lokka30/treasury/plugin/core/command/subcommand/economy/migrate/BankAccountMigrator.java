/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.account.BankAccountPermission;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.misc.TriState;
import org.jetbrains.annotations.NotNull;

class BankAccountMigrator implements AccountMigrator<BankAccount> {

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
    public @NotNull BiConsumer<@NotNull EconomyProvider, @NotNull EconomySubscriber<Collection<UUID>>> requestAccountIds() {
        return EconomyProvider::retrieveBankAccountIds;
    }

    @Override
    public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<BankAccount>> requestAccount() {
        return EconomyProvider::retrieveBankAccount;
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
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Phaser phaser,
            @NotNull BankAccount fromAccount,
            @NotNull BankAccount toAccount,
            @NotNull MigrationData migration
    ) {
        AccountMigrator.super.migrate(initiator, phaser, fromAccount, toAccount, migration);

        CompletableFuture<Collection<UUID>> memberUuidsFuture = new CompletableFuture<>();
        fromAccount.retrieveBankMembersIds(new PhasedFutureSubscriber<>(phaser, memberUuidsFuture));
        memberUuidsFuture.thenAccept(uuids -> {
            for (UUID uuid : uuids) {
                fromAccount.retrievePermissions(uuid, new EconomySubscriber<Map<BankAccountPermission, TriState>>() {
                    @Override
                    public void succeed(@NotNull final Map<BankAccountPermission, TriState> map) {
                        for (Map.Entry<BankAccountPermission, TriState> entry : map.entrySet()) {
                            toAccount.setPermission(uuid, entry.getValue(), new FailureConsumer<>(
                                    phaser,
                                    exception -> migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception))
                            ), entry.getKey());
                        }
                    }

                    @Override
                    public void fail(@NotNull final EconomyException exception) {
                        migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception));
                    }
                });
            }
        });
    }

    @Override
    public @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration) {
        return migration.bankAccountsProcessed();
    }

}
