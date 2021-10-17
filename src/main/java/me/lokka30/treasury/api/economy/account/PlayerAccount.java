/*
 * Copyright (c) 2021-2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.api.economy.account;

import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomyPublisher;
import me.lokka30.treasury.api.economy.response.EconomySubscription;
import org.jetbrains.annotations.NotNull;

/**
 * @author lokka30, Geolykt
 * @since v1.0.0
 * @see Account
 * A PlayerAccount is an Account owned by a Player.
 * Economy providers are likely to create player accounts
 * for players when they join the server, although
 * this is optional, which should be taken into consideration
 * when trying to access a player account which may not exist
 * yet for a player.
 */
@SuppressWarnings("unused")
public interface PlayerAccount extends Account {

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Account#resetBalance(Currency)
     * Resets the player's balance. Unlike resetting balances of non-player
     * and bank accounts, resetting a player account's balance will set the
     * player's balance to the 'starting balance' of the currency (other
     * accounts set it to zero instead). This is why the overriden method exists.
     * @param currency of the balance being reset
     */
    @Override
    default @NotNull EconomyPublisher<Double> resetBalance(@NotNull Currency currency) {
        final double newBalance = currency.getStartingBalance(null);
        return subscription -> setBalance(newBalance, currency).subscribe(
                new EconomySubscription<Double>() {
                    @Override
                    public void accept(@NotNull Double value) {
                        subscription.accept(newBalance);
                    }

                    @Override
                    public void error(@NotNull EconomyException exception) {
                        subscription.error(exception);
                    }
                }
        );
    }

}
