/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.migrate;

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

@SuppressWarnings("unused")
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
