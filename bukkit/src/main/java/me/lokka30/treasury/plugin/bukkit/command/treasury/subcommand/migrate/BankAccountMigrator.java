package me.lokka30.treasury.plugin.bukkit.command.treasury.subcommand.migrate;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

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
            @NotNull Phaser phaser,
            @NotNull BankAccount fromAccount,
            @NotNull BankAccount toAccount,
            @NotNull MigrationData migration) {
        AccountMigrator.super.migrate(phaser, fromAccount, toAccount, migration);

        CompletableFuture<Collection<UUID>> memberUuidsFuture = new CompletableFuture<>();
        fromAccount.retrieveBankMembersIds(new PhasedFutureSubscriber<>(phaser, memberUuidsFuture));
        memberUuidsFuture.thenAccept(uuids -> uuids.forEach(uuid -> toAccount.addBankMember(uuid,
                new FailureConsumer<>(phaser, exception -> migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception))))));

        CompletableFuture<Collection<UUID>> ownerUuidsFuture = new CompletableFuture<>();
        fromAccount.retrieveBankOwnersIds(new PhasedFutureSubscriber<>(phaser, ownerUuidsFuture));
        ownerUuidsFuture.thenAccept(uuids -> uuids.forEach(uuid -> toAccount.addBankOwner(uuid,
                new FailureConsumer<>(phaser, exception -> migration.debug(() -> getErrorLog(fromAccount.getUniqueId(), exception))))));
    }

    @Override
    public @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration) {
        return migration.bankAccountsProcessed();
    }

}
