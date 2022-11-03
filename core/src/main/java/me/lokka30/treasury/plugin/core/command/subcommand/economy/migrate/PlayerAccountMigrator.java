/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import org.jetbrains.annotations.NotNull;

class PlayerAccountMigrator implements AccountMigrator<PlayerAccount> {

    @Override
    public @NotNull String getBulkFailLog(final @NotNull FailureReason failureReason) {
        return "Unable to fetch player account UUIDs for migration: " + failureReason.getDescription();
    }

    @Override
    public @NotNull String getInitLog(@NotNull String identifier) {
        return "Migrating player account of UUID '&b" + identifier + "&7'.";
    }

    @Override
    public @NotNull String getErrorLog(
            @NotNull String identifier,
            @NotNull FailureReason failureReason
    ) {
        return "Error migrating account of player UUID '&b" + identifier + "&7': &b" + failureReason.getDescription();
    }

    @Override
    public @NotNull CompletableFuture<Response<Collection<String>>> requestAccountIds(@NotNull final EconomyProvider provider) {
        return provider.retrievePlayerAccountIds().thenApply(resp -> {
            if (!resp.isSuccessful()) {
                return Response.failure(resp.getFailureReason());
            }

            List<String> identifiers = new ArrayList<>();
            for (UUID uuid : resp.getResult()) {
                identifiers.add(uuid.toString());
            }
            return Response.success(identifiers);
        });
    }

    @Override
    public @NotNull CompletableFuture<Response<PlayerAccount>> requestOrCreateAccount(
            @NotNull final EconomyProvider provider, final String identifier
    ) {
        UUID uuid;
        try {
            uuid = UUID.fromString(identifier);
        } catch (IllegalArgumentException ignored) {
            return CompletableFuture.completedFuture(Response.failure(FailureReason.of(
                    "Invalid UUID of player account")));
        }
        return provider.accountAccessor().player().withUniqueId(uuid).get();
    }

    @Override
    public @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration) {
        return migration.playerAccountsProcessed();
    }

}
