/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.account.BankAccountPermission;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.response.FailureReason;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementors providing and managing economy data create a class
 * which implements this interface to be registered in
 * the specific platform they're implementing it for.
 *
 * @author lokka30, Jikoo, MrIvanPlays, NoahvdAa
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface EconomyProvider {

    /**
     * Get the version of the Treasury API the {@code EconomyProvider} is based on.
     *
     * <p>Warning: The Treasury API version is completely different to any other platform version,
     * such as the 'api-version' value in the Bukkit implementation's plugin.yml file.
     * <p>Warning: Do not use {@link EconomyAPIVersion#getCurrentAPIVersion()}, this is for internal Treasury use only.
     * <b>You must only use the constants provided.</b>
     *
     * @return the API version
     * @author lokka30, MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull EconomyAPIVersion getSupportedAPIVersion();

    /**
     * Check which optional Treasury Economy API features the Economy Provider supports.
     *
     * <p>There are a few features which Treasury allows Economy Providers to support
     * at their option. This is because certain features within Treasury may not be
     * suitable or appealing for some Economy Providers to implement. We try to enforce
     * as much of the API to be implemented as possible, however, few features can be
     * justified as being optional to implement.
     *
     * <p>The Economy Provider should return a Set of constants representing which of
     * Treasury's optional Economy API features are supported by the Economy Provider.
     * This set can be empty if none of the optional features are supported by the
     * Economy Provider.
     *
     * @return the set of optional supported features from the economy provider/
     * @author lokka30
     * @see OptionalEconomyApiFeature
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull Set<OptionalEconomyApiFeature> getSupportedOptionalEconomyApiFeatures();

    /**
     * Request whether a user has an associated {@link PlayerAccount}.
     *
     * @param accountId    the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void hasPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Request an existing {@link PlayerAccount} for a user.
     *
     * @param accountId    the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrievePlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription);

    /**
     * Request the creation of a {@link PlayerAccount} for a user.
     *
     * @param accountId    the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void createPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription);

    /**
     * Request all {@link UUID UUIDs} with associated {@link PlayerAccount PlayerAccounts}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrievePlayerAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request whether a {@link UUID} has an associated {@link BankAccount}.
     *
     * @param accountId    the {@code UUID} of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void hasBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Request an existing {@link BankAccount} for a {@link UUID}.
     *
     * @param accountId    the {@code UUID} of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription);

    /**
     * Request the creation of a {@link BankAccount} for a {@link UUID}.
     *
     * @param accountId    the {@code UUID} of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void createBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription) {
        createBankAccount(null, accountId, subscription);
    }

    /**
     * Request the creation of a {@link BankAccount} for a {@link UUID} {@code accountId} with {@link String} {@code name}.
     *
     * @param name         the name of the account
     * @param accountId    the {@code UUID} of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author MrIvanPlays, Jikoo
     * @since {@link EconomyAPIVersion#v1_0 v1.0}
     */
    void createBankAccount(@Nullable String name, @NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription);

    /**
     * Request all {@link UUID UUIDs} with associated {@link BankAccount BankAccounts}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveBankAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request all {@link BankAccount bank accounts} the given player is a member of.
     *
     * @param playerId     the player
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author MrNemo64, MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void retrieveAllBankAccountsPlayerIsMemberOf(
            @NotNull UUID playerId, @NotNull EconomySubscriber<Collection<UUID>> subscription
    ) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(subscription, "subscription");
        EconomySubscriber.asFuture(this::retrieveBankAccountIds).exceptionally(throwable -> {
            if (throwable instanceof EconomyException) {
                subscription.fail((EconomyException) throwable);
            } else {
                subscription.fail(new EconomyException(FailureReason.OTHER_FAILURE, throwable));
            }
            return new HashSet<>();
        }).thenAccept(uuids -> {
            if (uuids.isEmpty()) {
                subscription.succeed(uuids);
                return;
            }
            Set<UUID> ret = new HashSet<>();
            for (UUID uuid : uuids) {
                EconomySubscriber.<BankAccount>asFuture(subscriber -> retrieveBankAccount(uuid, subscriber)).exceptionally(
                        throwable -> {
                            if (throwable instanceof EconomyException) {
                                subscription.fail((EconomyException) throwable);
                            } else {
                                subscription.fail(new EconomyException(FailureReason.OTHER_FAILURE, throwable));
                            }
                            return null;
                        }).thenCompose(account -> {
                    if (account == null) {
                        return CompletableFuture.completedFuture(false);
                    }
                    return EconomySubscriber.asFuture(subscriber -> account.isBankMember(playerId, subscriber));
                }).thenAccept(val -> {
                    if (val) {
                        ret.add(uuid);
                    }
                });

            }
            subscription.succeed(ret);
        });
    }

    /**
     * Request all the {@link BankAccount bank accounts} where the given player has the given permissions.
     *
     * @param playerId     the player
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @param permissions  the permissions that the given player has to have on the {@link BankAccount account}
     * @author MrNemo64, MrIvanPlays
     * @see #retrieveAllBankAccountsPlayerIsMemberOf(UUID, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void retrieveAllBankAccountsPlayerHasPermission(
            @NotNull UUID playerId,
            @NotNull EconomySubscriber<Collection<UUID>> subscription,
            @NotNull BankAccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(subscription, "subscription");
        Objects.requireNonNull(permissions, "permissions");
        EconomySubscriber.asFuture(this::retrieveBankAccountIds).exceptionally(throwable -> {
            if (throwable instanceof EconomyException) {
                subscription.fail((EconomyException) throwable);
            } else {
                subscription.fail(new EconomyException(FailureReason.OTHER_FAILURE, throwable));
            }
            return new HashSet<>();
        }).thenAccept(uuids -> {
            if (uuids.isEmpty()) {
                subscription.succeed(uuids);
                return;
            }
            Set<UUID> ret = new HashSet<>();
            for (UUID uuid : uuids) {
                EconomySubscriber.<BankAccount>asFuture(subscriber -> retrieveBankAccount(uuid, subscriber)).exceptionally(
                        throwable -> {
                            if (throwable instanceof EconomyException) {
                                subscription.fail((EconomyException) throwable);
                            } else {
                                subscription.fail(new EconomyException(FailureReason.OTHER_FAILURE, throwable));
                            }
                            return null;
                        }).thenCompose(account -> {
                    if (account == null) {
                        return CompletableFuture.completedFuture(false);
                    }
                    return EconomySubscriber.asFuture(subscriber -> account.hasPermission(playerId, subscriber, permissions));
                }).thenAccept(val -> {
                    if (val) {
                        ret.add(uuid);
                    }
                });
            }
            subscription.succeed(ret);
        });
    }

    /**
     * Get the primary or main {@link Currency} of the economy.
     *
     * @return the primary currency
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull Currency getPrimaryCurrency();

    /**
     * Used to find a currency based on a specific identifier.
     *
     * @param identifier The {@link Currency#getIdentifier()} of the {@link Currency} we are searching for.
     * @return The {@link Optional} containing the search result. This will contain the
     * resulting {@link Currency} if it exists, otherwise it will return {@link Optional#empty()}.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    Optional<Currency> findCurrency(@NotNull String identifier);

    /**
     * Used to get a set of every  {@link Currency} object for the server.
     *
     * @return A set of every {@link Currency} object that is available for the server.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    Set<Currency> getCurrencies();

    /**
     * Get the String identifier of the primary or main {@link Currency} of the economy.
     *
     * @return the String identifier identifying the primary currency
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    default String getPrimaryCurrencyId() {
        return getPrimaryCurrency().getIdentifier();
    }

    /**
     * Used to register a currency with the {@link EconomyProvider} to be utilized by
     * other plugins.
     *
     * @param currency The currency to register with the {@link EconomyProvider}.
     * @param subscription The {@link EconomySubscriber} representing the result of the
     *                     attempted {@link Currency} registration with an {@link Boolean}.
     *                     This will be {@link Boolean#TRUE} if it was registered, otherwise
     *                     it'll be {@link Boolean#FALSE}.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void registerCurrency(@NotNull Currency currency, @NotNull EconomySubscriber<Boolean> subscription);
}
