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

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.exception.NonZeroAmountException;
import me.lokka30.treasury.api.economy.exception.OversizedWithdrawalException;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author lokka30
 * @since v1.0.0
 * @see EconomyProvider
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
     * @see Account#setBalance(double, String, Currency)
     * Get the balance of the Account.
     * @param worldName to get the balance of. Use an empty string to get the global balance.
     * @param currency of the balance being requested.
     * @return the balance of the account in specified world with specified currency.
     */
    double getBalance(@NotNull String worldName, @NotNull Currency currency);

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Account#getBalance(String, Currency)
     * Set the balance of the Account.
     * Specified amounts must be AT OR ABOVE zero.
     * @param amount of money the new balance will be.
     * @param worldName to set the new balance in. Use an empty string to modify the global balance.
     * @param currency of the balance being set.
     * @throws NonZeroAmountException if the amount is BELOW zero.
     */
    void setBalance(double amount, @NotNull String worldName, @NotNull Currency currency) throws NonZeroAmountException;

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Account#setBalance(double, String, Currency)
     * Withdraw an amount from the Account's balance.
     * Specified amounts must be ABOVE zero.
     * @param amount of money the account's current balance should be reduced by.
     * @param worldName to set the new balance in. Use an empty string to modify the global balance.
     * @param currency of the balance being set.
     * @throws NonZeroAmountException if the amount is AT OR BELOW zero.
     * @throws OversizedWithdrawalException if the NEW BALANCE is BELOW zero.
     */
    void withdrawBalance(double amount, @NotNull String worldName, @NotNull Currency currency) throws NonZeroAmountException, OversizedWithdrawalException;

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Account#setBalance(double, String, Currency)
     * Deposit an amount into the Account's balance.
     * Specified amounts must be ABOVE zero.
     * @param amount of money the account's current balance should be increased by.
     * @param worldName to set the new balance in. Use an empty string to modify the global balance.
     * @param currency of the balance being set.
     * @throws NonZeroAmountException if the amount is AT OR BELOW zero.
     */
    void depositBalance(double amount, @NotNull String worldName, @NotNull Currency currency) throws NonZeroAmountException;

    /**
     * @author lokka30
     * @since v1.0.0
     * @see PlayerAccount#resetBalance(String, Currency)
     * @see Account#setBalance(double, String, Currency)
     * Sets the Account's balance to `BigDecimal.ZERO`.
     * PlayerAccounts, by default, do not reset to `BigDecimal.ZERO` as they are overriden.
     * @param worldName to set the new balance in. Use an empty string to modify the global balance.
     * @param currency of the balance being set.
     * @throws NonZeroAmountException if the balance being reset to is BELOW zero.
     */
    default void resetBalance(@NotNull String worldName, @NotNull Currency currency) throws NonZeroAmountException {
        setBalance(0.0d, worldName, currency);
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
     * @throws NonZeroAmountException if the akmount is AT OR BELOW zero.
     */
    default boolean canAfford(double amount, @NotNull String worldName, @NotNull Currency currency) throws NonZeroAmountException {

        // amounts must be non-zero values
        if(amount <= 0) throw new NonZeroAmountException(amount);

        return getBalance(worldName, currency) >= amount;
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
