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

package me.lokka30.treasury.api.economy.currency;

import me.lokka30.treasury.api.economy.exception.NegativeAmountException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.UUID;

/**
 * @author lokka30, Geolykt
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
     * @author lokka30, Geolykt
     * @since v1.0.0
     * Get the starting balance of the currency.
     * The player UUID is nullable, it should be specified if the starting balance
     * concerns a PlayerAccount.
     * The world name is NOT nullable, if no world name is applicable then specify
     * null, where the economy provider is expected to supply a 'global'
     * balance instead.
     * @param playerUUID a UUID of the player account concerned. For global scenarios, specify `null`.
     * @param worldId of the world. For global scenarios, specify `null`.
     * @return the starting balance of the currency concerning specified player's UUID and world name.
     */
    double getStartingBalance(@Nullable UUID playerUUID, @Nullable UUID worldId);

    /**
     * Gets a human-readable format of the balance.
     * For example, '$1.50' or '1.50 dollars' or 'One dollar and fifty cents'.
     * @param amount to be formatted.
     * @param locale of the formatted balance being requested.
     * @throws NegativeAmountException if the amount is BELOW zero.
     * @return the human-readable format of the specified amount and locale.
     */
    @NotNull
    String formatBalance(double amount, @NotNull Locale locale) throws NegativeAmountException;
}
