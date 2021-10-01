/*
 * Copyright (c) 2021 lokka30.
 * This code is part of Treasury, an Economy API for Minecraft servers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You have received a copy of the GNU Affero General Public License
 * with this program - please see the LICENSE.md file. Alternatively,
 * please visit the <https://www.gnu.org/licenses/> website.
 *
 * Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 */

package me.lokka30.treasury.api.economy.account;

import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.exception.InvalidAmountException;
import org.jetbrains.annotations.NotNull;

/**
 * @author lokka30
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
     * @see Account#resetBalance(String, Currency)
     * Resets the player's balance. Unlike resetting balances of non-player
     * and bank accounts, resetting a player account's balance will set the
     * player's balance to the 'starting balance' of the currency (other
     * accounts set it to zero instead). This is why the overriden method exists.
     * @param worldName world name, or an empty string
     * @param currency of the balance being reset
     */
    @Override
    default void resetBalance(@NotNull String worldName, @NotNull Currency currency) throws InvalidAmountException {
        setBalance(currency.getStartingBalance(null, ""), worldName, currency);
    }

}
