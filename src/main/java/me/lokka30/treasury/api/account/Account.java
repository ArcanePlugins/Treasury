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

package me.lokka30.treasury.api.account;

import me.lokka30.treasury.api.currency.Currency;
import me.lokka30.treasury.api.exception.InvalidAmountException;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author lokka30
 * @since v1.0.0
 * @see me.lokka30.treasury.api.EconomyProvider
 * @see PlayerAccount
 * @see NonPlayerAccount
 * @see BankAccount
 * TODO A description about what an Account is in Treasury.
 */
@SuppressWarnings({"unused", "RedundantThrows"})
public interface Account {

    @NotNull UUID getUniqueId();

    @NotNull BigDecimal getBalance(String worldName, Currency currency);

    void setBalance(BigDecimal amount, String worldName, Currency currency) throws InvalidAmountException;

    void withdrawBalance(BigDecimal amount, String worldName, Currency currency) throws InvalidAmountException;

    void depositBalance(BigDecimal amount, String worldName, Currency currency) throws InvalidAmountException;

    default void resetBalance(String worldName, Currency currency) throws InvalidAmountException {
        try {
            setBalance(BigDecimal.ZERO, worldName, currency);
        } catch(InvalidAmountException ex) {
            // this should be an impossible exception
            ex.printStackTrace();
        }
    }

    default boolean canAfford(BigDecimal amount, String worldName, Currency currency) throws InvalidAmountException {

        // amounts must be non-zero values
        if(amount.compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException(amount);

        return getBalance(worldName, currency).compareTo(amount) >= 0;
    }

    void deleteAccount() throws UnsupportedOperationException;

}
