/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.response.EconomyFailureReason;
import org.jetbrains.annotations.NotNull;

// FIXME: Jikoo
class PlayerAccountMigrator implements AccountMigrator<PlayerAccount> {

    /*
    @Override
    public @NotNull String getBulkFailLog(@NotNull Throwable throwable) {
        return "Unable to fetch player account UUIDs for migration: " + throwable.getMessage();
    }

    @Override
    public @NotNull String getInitLog(@NotNull String identifier) {
        return "Migrating player account of UUID '&b" + identifier + "&7'.";
    }

    @Override
    public @NotNull String getErrorLog(@NotNull String identifier, @NotNull Throwable throwable) {
        return "Error migrating account of player UUID '&b" + identifier + "&7': &b" + throwable.getMessage();
    }

    @Override
    public @NotNull BiConsumer<@NotNull EconomyProvider, @NotNull EconomySubscriber<Collection<String>>> requestAccountIds() {
        return (provider, subscriber) -> {

            provider.retrievePlayerAccountIds(new EconomySubscriber<Collection<UUID>>() {
                @Override
                public void succeed(@NotNull final Collection<UUID> uuids) {
                    Collection<String> identifiers = new ArrayList<>();

                    uuids.forEach(uuid -> identifiers.add(uuid.toString()));

                    subscriber.succeed(identifiers);
                }

                @Override
                public void fail(@NotNull final EconomyException exception) {
                    subscriber.fail(exception);
                }
            });
        };
    }

    @Override
    public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull String, @NotNull EconomySubscriber<PlayerAccount>> requestAccount() {
        return (provider, identifier, subscription) -> {

            try {
                UUID uuid = UUID.fromString(identifier);
                provider.retrievePlayerAccount(uuid, subscription);
            } catch (Exception ignore) {
                subscription.fail(new EconomyException(
                        EconomyFailureReason.ACCOUNT_NOT_FOUND,
                        "Invalid UUID for player account."
                ));
            }
        };
    }

    @Override
    public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull String, @NotNull EconomySubscriber<Boolean>> checkAccountExistence() {
        return (provider, identifier, subscription) -> {

            try {
                UUID uuid = UUID.fromString(identifier);
                provider.hasPlayerAccount(uuid, subscription);
            } catch (Exception ignore) {
                subscription.fail(new EconomyException(
                        EconomyFailureReason.ACCOUNT_NOT_FOUND,
                        "Invalid UUID for player account."
                ));
            }

            provider.hasAccount(identifier, subscription);
        };
    }

    @Override
    public @NotNull TriConsumer<@NotNull EconomyProvider, @NotNull String, @NotNull EconomySubscriber<PlayerAccount>> createAccount() {
        return (provider, identifier, subscription) -> {

            try {
                UUID uuid = UUID.fromString(identifier);
                provider.createPlayerAccount(uuid, subscription);
            } catch (Exception ignore) {
                subscription.fail(new EconomyException(
                        EconomyFailureReason.ACCOUNT_NOT_FOUND,
                        "Invalid UUID for player account."
                ));
            }
        };
    }

    @Override
    public @NotNull AtomicInteger getSuccessfulMigrations(@NotNull MigrationData migration) {
        return migration.playerAccountsProcessed();
    }
     */

}
