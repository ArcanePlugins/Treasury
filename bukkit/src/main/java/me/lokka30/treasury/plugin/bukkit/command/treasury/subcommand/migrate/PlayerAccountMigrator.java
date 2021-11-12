package me.lokka30.treasury.plugin.bukkit.command.treasury.subcommand.migrate;

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
