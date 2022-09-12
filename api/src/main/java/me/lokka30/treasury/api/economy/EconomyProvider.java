/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomyFailureReason;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementors providing and managing economy data create a class
 * which implements this interface to be registered in
 * the specific platform they're implementing it for.
 *
 * @author lokka30, Jikoo, MrIvanPlays, NoahvdAa, creatorfromhell
 * @since v1.0.0
 */
public interface EconomyProvider {

    /**
     * Request whether a user has an associated {@link PlayerAccount}.
     *
     * @param accountId    the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void hasPlayerAccount(
            @NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription
    );

    /**
     * Request an existing {@link PlayerAccount} for a user.
     *
     * @param accountId    the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrievePlayerAccount(
            @NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription
    );

    /**
     * Request the creation of a {@link PlayerAccount} for a user.
     *
     * @param accountId    the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void createPlayerAccount(
            @NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription
    );

    /**
     * Request all {@link UUID UUIDs} with associated {@link PlayerAccount PlayerAccounts}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrievePlayerAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request whether an identifier has an associated {@link Account}.
     * <p>
     * This method is safe for {@link NonPlayerAccount non-player accounts}.
     * <p>
     * This could return an {@link NonPlayerAccount} or an {@link PlayerAccount}.
     *
     * @param identifier   the identifier of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void hasAccount(@NotNull String identifier, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Request an existing {@link Account} for a specific identifier.
     * <p>
     * This method is safe for {@link NonPlayerAccount non-player accounts}.
     * <p>
     * This could return an {@link NonPlayerAccount} or an {@link PlayerAccount}.
     *
     * @param identifier   the identifier of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrieveAccount(
            @NotNull String identifier, @NotNull EconomySubscriber<Account> subscription
    );

    /**
     * Request the creation of a {@link Account} for a specific identifier.
     * <p>
     * This method is safe for {@link NonPlayerAccount non-player accounts}.
     * <p>
     * This could return an {@link NonPlayerAccount} or an {@link PlayerAccount}.
     *
     * @param identifier   the identifier of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    default void createAccount(
            @NotNull String identifier, @NotNull EconomySubscriber<Account> subscription
    ) {
        createAccount(null, identifier, subscription);
    }

    /**
     * Request the creation of a {@link Account} for a specific identifier {@code identifier} with {@link String} {@code
     * name}.
     * <p>
     * This method is safe for {@link NonPlayerAccount non-player accounts}.
     * <p>
     * This could return an {@link NonPlayerAccount} or an {@link PlayerAccount}.
     *
     * @param name         the human-readable name of the account
     * @param identifier   the unique identifier of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void createAccount(
            @Nullable String name,
            @NotNull String identifier,
            @NotNull EconomySubscriber<Account> subscription
    );

    /**
     * Request all identifiers with associated {@link Account Accounts}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrieveAccountIds(@NotNull EconomySubscriber<Collection<String>> subscription);

    /**
     * Request all identifiers with associated {@link NonPlayerAccount NonPlayer Accounts}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrieveNonPlayerAccountIds(@NotNull EconomySubscriber<Collection<String>> subscription);

    /**
     * Request all {@link NonPlayerAccount non player accounts} the given player is a member of.
     *
     * @param playerId     the player
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    default void retrieveAllAccountsPlayerIsMemberOf(
            @NotNull UUID playerId, @NotNull EconomySubscriber<Collection<String>> subscription
    ) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(subscription, "subscription");
        EconomySubscriber.asFuture(this::retrieveAccountIds).exceptionally(throwable -> {
            if (throwable instanceof EconomyException) {
                subscription.fail((EconomyException) throwable);
            } else {
                subscription.fail(new EconomyException(EconomyFailureReason.OTHER_FAILURE,
                        throwable
                ));
            }
            return new HashSet<>();
        }).thenAccept(identifiers -> {
            if (identifiers.isEmpty()) {
                subscription.succeed(identifiers);
                return;
            }
            Set<String> ret = Collections.synchronizedSet(new HashSet<>());
            List<CompletableFuture<Void>> futures = new ArrayList<>(identifiers.size());
            for (String identifier : identifiers) {
                futures.add(EconomySubscriber.<Account>asFuture(
                        subscriber -> retrieveAccount(identifier, subscriber)
                ).thenCompose(account -> {
                    if (account == null) {
                        return CompletableFuture.completedFuture(false);
                    }

                    if (!(account instanceof NonPlayerAccount)) {
                        return CompletableFuture.completedFuture(false);
                    }
                    return EconomySubscriber.asFuture(subscriber -> account.isMember(playerId,
                            subscriber
                    ));
                }).thenAccept(val -> {
                    if (val) {
                        ret.add(identifier);
                    }
                }));
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenRun(() -> {
                        subscription.succeed(ret);
                    })
                    .exceptionally((throwable) -> {
                        if (throwable instanceof EconomyException) {
                            subscription.fail((EconomyException) throwable);
                        } else {
                            subscription.fail(new EconomyException(EconomyFailureReason.OTHER_FAILURE,
                                    throwable
                            ));
                        }
                        return null;
                    });
        });
    }

    /**
     * Request all the {@link NonPlayerAccount non-player accounts} where the given player has the given permissions.
     *
     * @param playerId     the player
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @param permissions  the permissions that the given player has to have on the {@link NonPlayerAccount account}
     * @see #retrieveAllAccountsPlayerIsMemberOf(UUID, EconomySubscriber)
     * @since v1.0.0
     */
    default void retrieveAllAccountsPlayerHasPermission(
            @NotNull UUID playerId,
            @NotNull EconomySubscriber<Collection<String>> subscription,
            @NotNull AccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(subscription, "subscription");
        Objects.requireNonNull(permissions, "permissions");
        EconomySubscriber.asFuture(this::retrieveAccountIds).exceptionally(throwable -> {
            if (throwable instanceof EconomyException) {
                subscription.fail((EconomyException) throwable);
            } else {
                subscription.fail(new EconomyException(EconomyFailureReason.OTHER_FAILURE,
                        throwable
                ));
            }
            return new HashSet<>();
        }).thenAccept(identifiers -> {
            if (identifiers.isEmpty()) {
                subscription.succeed(identifiers);
                return;
            }
            Set<String> ret = Collections.synchronizedSet(new HashSet<>());
            List<CompletableFuture<Void>> futures = new ArrayList<>(identifiers.size());
            for (String identifier : identifiers) {
                futures.add(EconomySubscriber.<Account>asFuture(
                        subscriber -> retrieveAccount(identifier, subscriber)
                ).thenCompose(account -> {
                    if (account == null) {
                        return CompletableFuture.completedFuture(false);
                    }

                    if (!(account instanceof NonPlayerAccount)) {
                        return CompletableFuture.completedFuture(false);
                    }

                    CompletableFuture<Boolean> ret1 = new CompletableFuture<>();
                    account.hasPermission(playerId, new EconomySubscriber<TriState>() {
                        @Override
                        public void succeed(@NotNull final TriState triState) {
                            ret1.complete(triState == TriState.TRUE);
                        }

                        @Override
                        public void fail(@NotNull final EconomyException exception) {
                            ret1.completeExceptionally(exception);
                        }
                    }, permissions);
                    return ret1;
                }).thenAccept(val -> {
                    if (val) {
                        ret.add(identifier);
                    }
                }));
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenRun(() -> {
                        subscription.succeed(ret);
                    })
                    .exceptionally((throwable) -> {
                        if (throwable instanceof EconomyException) {
                            subscription.fail((EconomyException) throwable);
                        } else {
                            subscription.fail(new EconomyException(EconomyFailureReason.OTHER_FAILURE,
                                    throwable
                            ));
                        }
                        return null;
                    });
        });
    }

    /**
     * Get the primary or main {@link Currency} of the economy.
     *
     * @return the primary currency
     * @since v1.0.0
     */
    @NotNull Currency getPrimaryCurrency();

    /**
     * Used to find a currency based on a specific identifier.
     *
     * @param identifier The {@link Currency#getIdentifier()} of the {@link Currency} we are searching for.
     * @return The {@link Optional} containing the search result. This will contain the
     *         resulting {@link Currency} if it exists, otherwise it will return {@link Optional#empty()}.
     * @since v1.0.0
     */
    Optional<Currency> findCurrency(@NotNull String identifier);

    /**
     * Used to get a set of every  {@link Currency} object for the server.
     *
     * @return A set of every {@link Currency} object that is available for the server.
     * @since v1.0.0
     */
    Set<Currency> getCurrencies();

    /**
     * Get the String identifier of the primary or main {@link Currency} of the economy.
     *
     * @return the String identifier identifying the primary currency
     * @since v1.0.0
     */
    @NotNull
    default String getPrimaryCurrencyId() {
        return getPrimaryCurrency().getIdentifier();
    }

    /**
     * Used to register a currency with the {@link EconomyProvider} to be utilized by
     * other plugins.
     *
     * @param currency     The currency to register with the {@link EconomyProvider}.
     * @param subscription The {@link EconomySubscriber} representing the result of the
     *                     attempted {@link Currency} registration with an {@link Boolean}.
     *                     This will be {@link Boolean#TRUE} if it was registered, otherwise
     *                     it'll be {@link Boolean#FALSE}.
     * @since v1.0.0
     */
    void registerCurrency(
            @NotNull Currency currency, @NotNull EconomySubscriber<Boolean> subscription
    );

}
