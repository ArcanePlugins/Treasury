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
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

class PlayerAccountMigrator implements AccountMigrator<PlayerAccount> {

    @Override
    public @NotNull String getBulkFailLog(@NotNull Throwable throwable) {
        return "Unable to fetch player account UUIDs for migration: " + throwable.getMessage();
    }

    @Override
    public @NotNull String getInitLog(@NotNull UUID uuid) {
        return "Migrating player account of UUID '&b" + uuid + "&7'.";
    }

    @Override
    public @NotNull String getErrorLog(@NotNull UUID uuid, @NotNull Throwable throwable) {
        return "Error migrating account of player UUID '&b" + uuid + "&7': &b" + throwable.getMessage();
    }

    @Override
    public @NotNull BiConsumer<@NotNull EconomyProvider, @NotNull EconomySubscriber<Collection<UUID>>> requestAccountIds() {
        return EconomyProvider::retrievePlayerAccountIds;
    }

    @Override
    public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<PlayerAccount>> requestAccount() {
        return EconomyProvider::retrievePlayerAccount;
    }

    @Override
    public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<Boolean>> checkAccountExistence() {
        return EconomyProvider::hasPlayerAccount;
    }

    @Override
    public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull UUID, @NotNull EconomySubscriber<PlayerAccount>> createAccount() {
        return EconomyProvider::createPlayerAccount;
    }

    @Override
    public @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration) {
        return migration.playerAccountsProcessed();
    }

}
