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

package me.lokka30.treasury.api.currency;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("unused")
public interface Currency {

    @NotNull
    String getCurrencyId();

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

    @NotNull
    String getCurrencyNameSingular();

    @NotNull
    String getCurrencyNamePlural();

    @NotNull
    String formatBalance(@NotNull BigDecimal amount);
}
