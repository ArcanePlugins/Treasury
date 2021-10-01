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
import me.lokka30.treasury.api.exception.OversizedWithdrawalException;
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
 * An Account is something that holds a balance and is associated with
 * something bound by a UUID. For example, a PlayerAccount is bound to
 * a Player on a server by their UUID.
 */
@SuppressWarnings({"unused", "RedundantThrows"})
public interface Account {

    /**
     * @author lokka30
     * @since v1.0.0
     * @see UUID
     * Get the UUID of the Account.
     * @return uuid of the Account.
     */
    @NotNull UUID getUniqueId();

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Account#setBalance(BigDecimal, String, Currency)
     * Get the balance of the Account.
     * @param worldName to get the balance of. Use an empty string to get the global balance.
     * @param currency of the balance being requested.
     * @return the balance of the account in specified world with specified currency.
     */
    @NotNull BigDecimal getBalance(@NotNull String worldName, @NotNull Currency currency);

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Account#getBalance(String, Currency)
     * Set the balance of the Account.
     * Specified amounts must be AT OR ABOVE zero.
     * @param amount of money the new balance will be.
     * @param worldName to set the new balance in. Use an empty string to modify the global balance.
     * @param currency of the balance being set.
     * @throws InvalidAmountException if the amount is BELOW zero.
     */
    void setBalance(@NotNull BigDecimal amount, @NotNull String worldName, @NotNull Currency currency) throws InvalidAmountException;

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Account#setBalance(BigDecimal, String, Currency)
     * Withdraw an amount from the Account's balance.
     * Specified amounts must be ABOVE zero.
     * @param amount of money the account's current balance should be reduced by.
     * @param worldName to set the new balance in. Use an empty string to modify the global balance.
     * @param currency of the balance being set.
     * @throws InvalidAmountException if the amount is AT OR BELOW zero.
     * @throws OversizedWithdrawalException if the NEW BALANCE is BELOW zero.
     */
    void withdrawBalance(@NotNull BigDecimal amount, @NotNull String worldName, @NotNull Currency currency) throws InvalidAmountException, OversizedWithdrawalException;

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Account#setBalance(BigDecimal, String, Currency)
     * Deposit an amount into the Account's balance.
     * Specified amounts must be ABOVE zero.
     * @param amount of money the account's current balance should be increased by.
     * @param worldName to set the new balance in. Use an empty string to modify the global balance.
     * @param currency of the balance being set.
     * @throws InvalidAmountException if the amount is AT OR BELOW zero.
     */
    void depositBalance(@NotNull BigDecimal amount, @NotNull String worldName, @NotNull Currency currency) throws InvalidAmountException;

    /**
     * @author lokka30
     * @since v1.0.0
     * @see PlayerAccount#resetBalance(String, Currency)
     * @see Account#setBalance(BigDecimal, String, Currency)
     * Sets the Account's balance to `BigDecimal.ZERO`.
     * PlayerAccounts, by default, do not reset to `BigDecimal.ZERO` as they are overriden.
     * @param worldName to set the new balance in. Use an empty string to modify the global balance.
     * @param currency of the balance being set.
     * @throws InvalidAmountException if the balance being reset to is BELOW zero.
     */
    default void resetBalance(@NotNull String worldName, @NotNull Currency currency) throws InvalidAmountException {
        setBalance(BigDecimal.ZERO, worldName, currency);
    }

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Account#getBalance(String, Currency)
     * Check if the Account can afford a withdrawal of a certain amount.
     * Specified amounts must be ABOVE zero.
     * @param amount of money proposed for withdrawal.
     * @param worldName of the balance being requested in.
     * @param currency of the balance being requested.
     * @return whether the Account can afford the withdrawal.
     * @throws InvalidAmountException if the akmount is AT OR BELOW zero.
     */
    default boolean canAfford(@NotNull BigDecimal amount, @NotNull String worldName, @NotNull Currency currency) throws InvalidAmountException {

        // amounts must be non-zero values
        if(amount.compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException(amount);

        return getBalance(worldName, currency).compareTo(amount) >= 0;
    }

    /**
     * @author lokka30
     * @since v1.0.0
     * Deletes the Account's data.
     * Providers should consider storing backups of deleted accounts.
     * @throws UnsupportedOperationException if the Provider does not support the deletion of this Account.
     */
    void deleteAccount() throws UnsupportedOperationException;

}
