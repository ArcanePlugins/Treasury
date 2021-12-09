/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;

/**
 * A PlayerAccount is an Account owned by a Player.
 * Economy providers are likely to create player accounts
 * for players when they join the server, although
 * this is optional, which should be taken into consideration
 * when trying to access a player account which may not exist
 * yet for a player.
 *
 * @author lokka30, Geolykt
 * @see Account
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface PlayerAccount extends Account {

    /**
     * Resets the player's balance. Unlike resetting balances of non-player
     * and bank accounts, resetting a player account's balance will set the
     * player's balance to the 'starting balance' of the currency (other
     * accounts set it to zero instead). This is why the overriden method exists.
     *
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     * @see Account#resetBalance(Currency, EconomySubscriber)
     * @param currency of the balance being reset
     */
    @Override
    default void resetBalance(@NotNull Currency currency, @NotNull EconomySubscriber<Double> subscription) {
        final double newBalance = currency.getStartingBalance(null);
        setBalance(newBalance, currency, new EconomySubscriber<Double>() {
                @Override
                public void succeed(@NotNull Double value) {
                    subscription.succeed(newBalance);
                }

                @Override
                public void fail(@NotNull EconomyException exception) {
                    subscription.fail(exception);
                }
            }
        );
    }

}
