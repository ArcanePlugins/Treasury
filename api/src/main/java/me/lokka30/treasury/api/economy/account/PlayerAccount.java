/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyFailureReason;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionImportance;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A PlayerAccount is an Account owned by a Player.
 * Economy providers are likely to create player accounts
 * for players when they join the server, although
 * this is optional, which should be taken into consideration
 * when trying to access a player account which may not exist
 * yet for a player.
 *
 * @author lokka30, Geolykt, creatorfromhell
 * @see Account
 * @since v1.0.0
 */
public interface PlayerAccount extends Account {

    /**
     * Returns a map fulfilled with all {@link AccountPermission} with {@link TriState} values of
     * {@link TriState#TRUE}.
     */
    Map<AccountPermission, TriState> ALL_PERMISSIONS_MAP = Collections.unmodifiableMap(Arrays
            .stream(AccountPermission.values())
            .collect(Collectors.toConcurrentMap(p -> p, $ -> TriState.TRUE)));

    /**
     * Gets the string-based unique identifier for this account.
     *
     * @return The String unique identifier for this account.
     * @since v1.0.0
     */
    @Override
    default @NotNull String getIdentifier() {
        return getUniqueId().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default CompletableFuture<Response<TriState>> setName(@Nullable String name) {
        return CompletableFuture.completedFuture(Response.success(TriState.FALSE));
    }

    /**
     * Get the {@link UUID} of the {@code Account}.
     *
     * @return uuid of the Account.
     * @see UUID
     * @since v1.0.0
     */
    @NotNull UUID getUniqueId();

    /**
     * {@inheritDoc}
     */
    @Override
    default CompletableFuture<Response<TriState>> isMember(@NotNull UUID player) {
        Objects.requireNonNull(player, "player");
        return CompletableFuture.completedFuture(Response.success(getUniqueId().equals(player)
                ? TriState.TRUE
                : TriState.UNSPECIFIED));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default CompletableFuture<Response<Collection<UUID>>> retrieveMemberIds() {
        return CompletableFuture.completedFuture(Response.success(Collections.singletonList(
                getUniqueId())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default CompletableFuture<Response<TriState>> hasPermission(
            @NotNull UUID player, @NotNull AccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(permissions, "permissions");

        return CompletableFuture.completedFuture(Response.success(TriState.fromBoolean(getUniqueId().equals(
                player))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default CompletableFuture<Response<Map<AccountPermission, TriState>>> retrievePermissions(
            @NotNull UUID player
    ) {
        Objects.requireNonNull(player, "player");

        return CompletableFuture.completedFuture(Response.success(getUniqueId().equals(player)
                ? ALL_PERMISSIONS_MAP
                : Collections.emptyMap()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Response<Map<UUID, Set<Map.Entry<AccountPermission, TriState>>>>> retrievePermissionsMap() {
        return CompletableFuture.completedFuture(Response.success(Collections.singletonMap(
                this.getUniqueId(),
                ALL_PERMISSIONS_MAP.entrySet()
        )));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default CompletableFuture<Response<TriState>> setPermission(
            @NotNull UUID player,
            @NotNull TriState permissionValue,
            @NotNull AccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(player, "player");

        return CompletableFuture.completedFuture(Response.failure(EconomyFailureReason.PLAYER_ACCOUNT_PERMISSION_MODIFICATION_NOT_SUPPORTED));
    }

    /**
     * Resets the player's balance. Unlike resetting balances of non-player
     * and non player accounts, resetting a player account's balance will set the
     * player's balance to the 'starting balance' of the currency (other
     * accounts set it to zero instead). This is why the overridden method exists.
     *
     * @param initiator the one who initiated this transaction
     * @param currency  of the balance being reset
     * @return see {@link Account#resetBalance(EconomyTransactionInitiator, Currency, EconomyTransactionImportance, String)}
     * @see Account#resetBalance(EconomyTransactionInitiator, Currency, EconomyTransactionImportance, String)
     * @since v1.0.0
     */
    @Override
    default CompletableFuture<Response<BigDecimal>> resetBalance(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomyTransactionImportance importance,
            @Nullable String reason
    ) {
        Objects.requireNonNull(initiator, "initiator");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(importance, "importance");

        return doTransaction(EconomyTransaction
                .newBuilder()
                .withCurrency(currency)
                .withInitiator(initiator)
                .withTransactionAmount(currency.getStartingBalance(getUniqueId()))
                .withReason(reason)
                .withImportance(importance)
                .withTransactionType(EconomyTransactionType.SET)
                .build());
    }

}
