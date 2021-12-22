/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.misc.TriState;
import org.jetbrains.annotations.NotNull;

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
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface PlayerAccount extends Account {

    /**
     * Returns a map fulfilled with all {@link AccountPermission} with {@link TriState} values of
     * {@link TriState#TRUE}
     */
    Map<AccountPermission, TriState> ALL_PERMISSIONS_MAP = Collections.unmodifiableMap(new ConcurrentHashMap<AccountPermission, TriState>() {
        {
            for (AccountPermission permission : AccountPermission.values()) {
                put(permission, TriState.TRUE);
            }
        }
    });

    /**
     * Gets the string-based unique identifier for this account.
     *
     * @return The String unique identifier for this account.
     * @author creatorfromhell
     * @since {@link EconomyAPIVersion#v1_0 v1.0}
     */
    @Override
    default @NotNull String getIdentifier() {
        return getUniqueId().toString();
    }

    /**
     * Get the {@link UUID} of the {@code Account}.
     *
     * @return uuid of the Account.
     * @author lokka30
     * @see UUID
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
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
            @NotNull EconomySubscriber<Boolean> subscription,
            @NotNull AccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(subscription, "subscription");
        subscription.succeed(getUniqueId().equals(player));
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
            @NotNull EconomySubscriber<Boolean> subscription,
            @NotNull AccountPermission @NotNull ... permissions
    ) {
        // do nothing. The default interaction is that a consumer plugin may not modify the
        // permissions of a player account, although is left for the provider plugin to override this interaction.
    }

    /**
     * Resets the player's balance. Unlike resetting balances of non-player
     * and non player accounts, resetting a player account's balance will set the
     * player's balance to the 'starting balance' of the currency (other
     * accounts set it to zero instead). This is why the overriden method exists.
     *
     * @param initiator    the one who initiated this transaction
     * @param currency     of the balance being reset
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @author lokka30, MrIvanPlays, creatorfromhell
     * @see Account#resetBalance(EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @Override
    default void resetBalance(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomySubscriber<BigDecimal> subscription
    ) {
        final BigDecimal newBalance = currency.getStartingBalance(null);
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
