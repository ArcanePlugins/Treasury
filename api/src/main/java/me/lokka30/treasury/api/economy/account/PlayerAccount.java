/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomyFailureReason;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
// todo: they;'re here too

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
    default void setName(
            @Nullable String name, @NotNull EconomySubscriber<Boolean> subscription
    ) {
        subscription.succeed(false);
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
    default void isMember(@NotNull UUID player, @NotNull EconomySubscriber<Boolean> subscription) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(subscription, "subscription");
        subscription.succeed(getUniqueId().equals(player));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void retrieveMemberIds(@NotNull EconomySubscriber<Collection<UUID>> subscription) {
        Objects.requireNonNull(subscription, "subscription");
        subscription.succeed(Collections.singletonList(getUniqueId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void hasPermission(
            @NotNull UUID player,
            @NotNull EconomySubscriber<TriState> subscription,
            @NotNull AccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(subscription, "subscription");
        Objects.requireNonNull(permissions, "permissions");

        subscription.succeed(TriState.fromBoolean(player.equals(getUniqueId())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void retrievePermissions(
            @NotNull UUID player,
            @NotNull EconomySubscriber<Map<AccountPermission, TriState>> subscription
    ) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(subscription, "subscription");
        subscription.succeed((getUniqueId().equals(player)
                ? ALL_PERMISSIONS_MAP
                : Collections.emptyMap()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void setPermission(
            @NotNull UUID player,
            @NotNull TriState permissionValue,
            @NotNull EconomySubscriber<TriState> subscription,
            @NotNull AccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(subscription, "subscription");

        subscription.fail(new EconomyException(EconomyFailureReason.PLAYER_ACCOUNT_PERMISSION_MODIFICATION_NOT_SUPPORTED));
    }

    /**
     * Resets the player's balance. Unlike resetting balances of non-player
     * and non player accounts, resetting a player account's balance will set the
     * player's balance to the 'starting balance' of the currency (other
     * accounts set it to zero instead). This is why the overridden method exists.
     *
     * @param initiator    the one who initiated this transaction
     * @param currency     of the balance being reset
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @see Account#resetBalance(EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @since v1.0.0
     */
    @Override
    default void resetBalance(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomySubscriber<BigDecimal> subscription
    ) {
        Objects.requireNonNull(initiator, "initiator");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(subscription, "subscription");

        final BigDecimal newBalance = currency.getStartingBalance(getUniqueId());
        setBalance(newBalance, initiator, currency, new EconomySubscriber<BigDecimal>() {
            @Override
            public void succeed(@NotNull BigDecimal value) {
                subscription.succeed(newBalance);
            }

            @Override
            public void fail(@NotNull EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

}
