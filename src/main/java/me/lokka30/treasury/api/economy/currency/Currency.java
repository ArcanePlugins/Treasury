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

package me.lokka30.treasury.api.economy.currency;

import me.lokka30.treasury.api.economy.exception.InvalidAmountException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

/**
 * @author lokka30
 * @since v1.0.0
 * One of Treasury's core features is multi-currency support.
 * This allows economy providers and plugins to use different
 * currencies for different transactions, e.g. a daily reward
 * plugin can award players 'Tokens', but a job plugin
 * can award players 'Dollars'. Facilitates great customisability.
 */
@SuppressWarnings("unused")
public interface Currency {

    /**
     * @author lokka30
     * @since v1.0.0
     * Get the UUID of the currency.
     * @return the UUID of the currency.
     */
    @NotNull
    UUID getCurrencyId();

    /**
     * @author lokka30
     * @since v1.0.0
     * Get the name of the currency, e.g. 'dollars' or 'tokens'.
     * @return the name of the currency.
     */
    @NotNull
    String getCurrencyName();

    /**
     * @author lokka30
     * @since v1.0.0
     * Some economy providers like to round balances' decimals.
     * Economy providers that do not round any digits should specify `-1`.
     * @return how many rounded digits the provider uses, or `-1` for none
     */
    int getRoundedDigits();

    /**
     * @author lokka30
     * @since v1.0.0
     * Get the starting balance of the currency.
     * The player UUID is nullable, it should be specified if the starting balance
     * concerns a PlayerAccount.
     * The world name is NOT nullable, if no world name is applicable then specify
     * an empty string, where the economy provider is expected to supply a 'global'
     * balance instead.
     * @param playerUUID a UUID of the player account concerned, otherwise, specify `null`.
     * @param worldName a non-null world name. Use an empty string if no world name is applicable.
     * @return the starting balance of the currency concerning specified player's UUID and world name.
     */
    @NotNull
    BigDecimal getStartingBalance(@Nullable UUID playerUUID, @NotNull String worldName);

    /**
     * Gets a human-readable format of the balance.
     * For example, '$1.50' or '1.50 dollars' or 'One dollar and fifty cents'.
     * @param amount to be formatted.
     * @param locale of the formatted balance being requested.
     * @throws InvalidAmountException if the amount is BELOW zero.
     * @return the human-readable format of the specified amount and locale.
     */
    @NotNull
    String formatBalance(@NotNull BigDecimal amount, @NotNull Locale locale) throws InvalidAmountException;
}
