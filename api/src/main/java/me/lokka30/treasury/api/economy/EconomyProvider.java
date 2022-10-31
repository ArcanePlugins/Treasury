/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.Response;
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
     * @param accountId the {@link UUID} of the account owner
     * @return future with {@link Response} which if successful returns the resulting {@link TriState}
     * @since v1.0.0
     */
    CompletableFuture<Response<TriState>> hasPlayerAccount(@NotNull UUID accountId);

    /**
     * Request an existing {@link PlayerAccount} for a user.
     *
     * @param accountId the {@link UUID} of the account owner
     * @return future with {@link Response} which if successful returns the resulting
     *         {@link PlayerAccount}
     * @since v1.0.0
     */
    CompletableFuture<Response<PlayerAccount>> retrievePlayerAccount(@NotNull UUID accountId);

    /**
     * Request the creation of a {@link PlayerAccount} for a user.
     *
     * @param accountId the {@link UUID} of the account owner
     * @return future with {@link Response} which if successful returns the resulting
     *         {@link PlayerAccount}
     * @since v1.0.0
     */
    CompletableFuture<Response<PlayerAccount>> createPlayerAccount(@NotNull UUID accountId);

    /**
     * Request all {@link UUID UUIDs} with associated {@link PlayerAccount PlayerAccounts}.
     *
     * @return future with {@link Response} which if successful returns the resulting value
     * @since v1.0.0
     */
    CompletableFuture<Response<Collection<UUID>>> retrievePlayerAccountIds();

    /**
     * Request whether an identifier has an associated {@link Account}.
     * <p>
     * This method is safe for {@link NonPlayerAccount non-player accounts}.
     * <p>
     * This could return an {@link NonPlayerAccount} or an {@link PlayerAccount}.
     *
     * @param identifier the identifier of the account
     * @return future with {@link Response} which if successful returns the resulting value
     * @since v1.0.0
     */
    CompletableFuture<Response<TriState>> hasAccount(@NotNull String identifier);

    /**
     * Request an existing {@link Account} for a specific identifier.
     * <p>
     * This method is safe for {@link NonPlayerAccount non-player accounts}.
     * <p>
     * This could return an {@link NonPlayerAccount} or an {@link PlayerAccount}.
     *
     * @param identifier the identifier of the account
     * @return future with {@link Response} which if successful returns the resulting value
     * @since v1.0.0
     */
    CompletableFuture<Response<Account>> retrieveAccount(@NotNull String identifier);

    /**
     * Request the creation of a {@link Account} for a specific identifier.
     * <p>
     * This method is safe for {@link NonPlayerAccount non-player accounts}.
     * <p>
     * This could return an {@link NonPlayerAccount} or an {@link PlayerAccount}.
     *
     * @param identifier the identifier of the account
     * @return future with {@link Response} which if successful returns the resulting value
     * @since v1.0.0
     */
    default CompletableFuture<Response<Account>> createAccount(@NotNull String identifier) {
        return createAccount(null, identifier);
    }

    /**
     * Request the creation of a {@link Account} for a specific identifier {@code identifier} with {@link String} {@code
     * name}.
     * <p>
     * This method is safe for {@link NonPlayerAccount non-player accounts}.
     * <p>
     * This could return an {@link NonPlayerAccount} or an {@link PlayerAccount}.
     *
     * @param name       the human-readable name of the account
     * @param identifier the unique identifier of the account
     * @return future with {@link Response} which if successful returns the resulting value
     * @since v1.0.0
     */
    CompletableFuture<Response<Account>> createAccount(
            @Nullable String name, @NotNull String identifier
    );

    /**
     * Request all identifiers with associated {@link Account Accounts}.
     *
     * @return future with {@link Response} which if successful returns the resulting value
     * @since v1.0.0
     */
    CompletableFuture<Response<Collection<String>>> retrieveAccountIds();

    /**
     * Request all identifiers with associated {@link NonPlayerAccount NonPlayer Accounts}.
     *
     * @return future with {@link Response} which if successful returns the resulting value
     * @since v1.0.0
     */
    CompletableFuture<Response<Collection<String>>> retrieveNonPlayerAccountIds();

    /**
     * Request all {@link NonPlayerAccount non player accounts} the given player is a member of.
     *
     * @param playerId the player
     * @return future with {@link Response} which if successful returns the resulting value
     * @since v1.0.0
     */
    default CompletableFuture<Collection<String>> retrieveAllAccountsPlayerIsMemberOf(@NotNull UUID playerId) {
        Objects.requireNonNull(playerId, "playerId");

        return retrieveAccountIds().thenCompose(result -> {
            if (!result.isSuccessful() || result.getResult().isEmpty()) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }
            Collection<String> identifiers = result.getResult();
            Collection<CompletableFuture<Response<Account>>> accountFutures =
                    new ArrayList<>(identifiers.size());
            for (String identifier : identifiers) {
                accountFutures.add(retrieveAccount(identifier));
            }
            return FutureHelper.mapJoinFilter(
                    res -> {
                        if (!res.isSuccessful()) {
                            return CompletableFuture.completedFuture(TriState.FALSE);
                        } else {
                            return res.getResult().isMember(playerId).thenCompose(res1 -> {
                                if (!res1.isSuccessful()) {
                                    return CompletableFuture.completedFuture(TriState.FALSE);
                                }
                                return CompletableFuture.completedFuture(res1.getResult());
                            });
                        }
                    },
                    res -> res.getResult().getIdentifier(),
                    accountFutures
            );
        });
    }

    /**
     * Request all the {@link NonPlayerAccount non-player accounts} where the given player has the given permissions.
     *
     * @param playerId    the player
     * @param permissions the permissions that the given player has to have on the {@link NonPlayerAccount account}
     * @return future with {@link Response} which if successful returns the resulting value
     * @see #retrieveAllAccountsPlayerIsMemberOf(UUID)
     * @since v1.0.0
     */
    default CompletableFuture<Collection<String>> retrieveAllAccountsPlayerHasPermission(
            @NotNull UUID playerId, @NotNull AccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(permissions, "permissions");

        return retrieveAccountIds().thenCompose(result -> {
            if (!result.isSuccessful() || result.getResult().isEmpty()) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }
            Collection<String> identifiers = result.getResult();
            Collection<CompletableFuture<Response<Account>>> accountFutures =
                    new ArrayList<>(identifiers.size());
            for (String identifier : identifiers) {
                accountFutures.add(retrieveAccount(identifier));
            }
            return FutureHelper.mapJoinFilter(
                    res -> {
                        if (!res.isSuccessful()) {
                            return CompletableFuture.completedFuture(TriState.FALSE);
                        } else {
                            return res.getResult().hasPermission(playerId, permissions).thenCompose(res1 -> {
                                if (!res1.isSuccessful()) {
                                    return CompletableFuture.completedFuture(TriState.FALSE);
                                }
                                return CompletableFuture.completedFuture(res1.getResult());
                            });
                        }
                    },
                    res -> res.getResult().getIdentifier(),
                    accountFutures
            );
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
     * @param currency The currency to register with the {@link EconomyProvider}.
     * @return future with {@link Response} which if successful returns a {@link TriState}
     *         whether the registration was successful. If the currency was successfully registered, this
     *         shall be {@link TriState#TRUE}, otherwise {@link TriState#FALSE} and if that currency is
     *         already registered, {@link TriState#UNSPECIFIED}
     * @since v1.0.0
     */
    CompletableFuture<Response<TriState>> registerCurrency(@NotNull Currency currency);

}
