/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.util.UUID;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.misc.TriState;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;

/**
 * Enum that holds the permissions of a {@link BankAccount}.
 *
 * @author MrNemo64
 * @see BankAccount
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public enum BankAccountPermission {

    /**
     * Allows a player to consult the balance of the {@link BankAccount}.
     *
     * @see Account#retrieveBalance(Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    CONSULT,

    /**
     * Allows a player to withdraw from the {@link BankAccount}.
     *
     * @see Account#withdrawBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    WITHDRAW,

    /**
     * Allows a player to deposit on the {@link BankAccount}.
     *
     * @see Account#depositBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    DEPOSIT,

    /**
     * Allows a player to modify the permissions of other players on a {@link BankAccount}
     *
     * @see BankAccount#setPermission(UUID, TriState, EconomySubscriber, BankAccountPermission...)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    MODIFY_PERMISSIONS
}
