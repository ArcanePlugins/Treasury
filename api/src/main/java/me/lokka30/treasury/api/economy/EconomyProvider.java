/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.AccountData;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementors providing and managing economy data create a class which implements this
 * interface to be registered in Treasury's {@link me.lokka30.treasury.api.common.service
 * Services API}.
 *
 * @author lokka30, Jikoo, MrIvanPlays, NoahvdAa, creatorfromhell
 * @since v1.0.0
 */
public interface EconomyProvider {

    /**
     * Returns the {@link AccountAccessor} of this {@code EconomyProvider}.
     * <p>The account accessor serves the purpose of creating or retrieving existing accounts.
     * Please check its documentation for more in-depth explanation.
     *
     * @return account accessor
     * @see AccountAccessor
     * @see me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor
     * @see me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor
     * @since 2.0.0
     */
    @NotNull AccountAccessor accountAccessor();

    /**
     * Request whether the specified {@link AccountData} has an associated {@link Account}.
     * <br>
     * This is here for edge case situations, where calling the {@link #accountAccessor()} does
     * not make sense. Per standard, calling the {@link #accountAccessor()} will give an account,
     * whether just created or pulled from a database.
     *
     * @param accountData data about the account type and specific account identifiers
     * @return whether there is an account registered with the given identification from the account data
     * @see AccountData
     * @since 2.0.0
     */
    @NotNull CompletableFuture<Boolean> hasAccount(@NotNull AccountData accountData);

    /**
     * Request all {@link UUID UUIDs} with associated {@link PlayerAccount PlayerAccounts}.
     *
     * @return a collection of player account uuids
     * @since v1.0.0
     */
    @NotNull CompletableFuture<Collection<UUID>> retrievePlayerAccountIds();

    /**
     * Request all {@link NamespacedKey identifiers} with associated {@link NonPlayerAccount
     * NonPlayer Accounts}.
     *
     * @return a collection of non-player account namespaced key identifiers
     * @since v1.0.0
     */
    @NotNull CompletableFuture<Collection<NamespacedKey>> retrieveNonPlayerAccountIds();

    /**
     * Request all {@link NonPlayerAccount non player accounts} the given player is a member of.
     *
     * @param playerId the player
     * @return a collection of all accounts that the given player is a member of
     * @since v1.0.0
     */
    @NotNull
    default CompletableFuture<Collection<NonPlayerAccount>> retrieveAllAccountsPlayerIsMemberOf(@NotNull UUID playerId) {
        Objects.requireNonNull(playerId, "playerId");

        return retrieveNonPlayerAccountIds().thenCompose(identifiers -> {
            Collection<CompletableFuture<NonPlayerAccount>> accountFutures = new ArrayList<>(
                    identifiers.size());
            for (NamespacedKey identifier : identifiers) {
                accountFutures.add(this
                        .accountAccessor()
                        .nonPlayer()
                        .withIdentifier(identifier)
                        .get());
            }
            return FutureHelper.joinAndFilter(
                    account -> account
                            .isMember(playerId)
                            .thenCompose(val -> CompletableFuture.completedFuture(TriState.fromBoolean(
                                    val))),
                    accountFutures
            );
        });
    }

    /**
     * Request all the {@link NonPlayerAccount non-player accounts} where the given player has the given permissions.
     *
     * @param playerId    the player
     * @param permissions the permissions that the given player has to have on the {@link NonPlayerAccount account}
     * @return a collection of all accounts that the given player has all given permission(s) in
     * @see #retrieveAllAccountsPlayerIsMemberOf(UUID)
     * @since v1.0.0
     */
    @NotNull
    default CompletableFuture<Collection<NonPlayerAccount>> retrieveAllAccountsPlayerHasPermissions(
            @NotNull UUID playerId, @NotNull AccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(permissions, "permissions");

        return retrieveNonPlayerAccountIds().thenCompose(identifiers -> {
            Collection<CompletableFuture<NonPlayerAccount>> accountFutures = new ArrayList<>(
                    identifiers.size());
            for (NamespacedKey identifier : identifiers) {
                accountFutures.add(this
                        .accountAccessor()
                        .nonPlayer()
                        .withIdentifier(identifier)
                        .get());
            }
            return FutureHelper.joinAndFilter(account -> account.hasPermissions(
                    playerId,
                    permissions
            ), accountFutures);
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
    @NotNull Optional<Currency> findCurrency(@NotNull String identifier);

    /**
     * Used to find a currency based on its display name.
     * <p>We <b>strongly</b> encourage economy implementors to override this and provide a better
     * implementation.
     *
     * @param displayName the {@link Currency#getDisplayName(BigDecimal, Locale)} of the
     *                    {@link Currency} we are searching for.
     * @param value       whether we're going to compare against a singular or a plural display name of a
     *                    currency
     * @param locale      a locale
     * @return the {@link Optional} containing the search result. This will contain the resulting
     *         {@link Currency} if it exists, otherwise it will return {@link Optional#empty()}
     * @since 2.0.1
     */
    @NotNull
    default Optional<Currency> findCurrencyByDisplayName(
            @NotNull String displayName, @NotNull BigDecimal value, @Nullable Locale locale
    ) {
        for (Currency currency : getCurrencies()) {
            if (currency.getDisplayName(value, locale).equalsIgnoreCase(displayName)) {
                return Optional.of(currency);
            }
        }
        return Optional.empty();
    }

    /**
     * Used to get a set of every  {@link Currency} object for the server.
     *
     * @return A set of every {@link Currency} object that is available for the server.
     * @since v1.0.0
     */
    @NotNull Set<Currency> getCurrencies();

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
     * @param currency The currency to register with the {@link EconomyProvider}.
     * @return a {@link TriState} value representing whether the registration was successful. If
     *         the currency was successfully registered, this shall be {@link TriState#TRUE}, otherwise
     *         {@link TriState#FALSE} and if that currency is already registered,
     *         {@link TriState#UNSPECIFIED}.
     * @since v1.0.0
     */
    @NotNull CompletableFuture<TriState> registerCurrency(@NotNull Currency currency);

    /**
     * Used to un-register a currency with the {@link EconomyProvider}.
     *
     * @param currency The currency to un-register with the {@link EconomyProvider}.
     * @return a {@link TriState} value representing whether the unregistration was successful. If
     *         the currency was successfully unregistered, {@link TriState#TRUE} is returned,
     *         otherwise {@link TriState#FALSE}, and if that currency is already not registered,
     *         {@link TriState#UNSPECIFIED} is returned.
     * @since v2.0.0
     */
    @NotNull CompletableFuture<TriState> unregisterCurrency(@NotNull Currency currency);

}
