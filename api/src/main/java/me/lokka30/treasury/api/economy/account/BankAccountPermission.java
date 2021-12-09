/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;

import java.util.UUID;

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
     * @see Account#withdrawBalance(double, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    WITHDRAW,

    /**
     * Allows a player to deposit on the {@link BankAccount}.
     *
     * @see Account#depositBalance(double, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    DEPOSIT,

    /**
     * Allows a player to add members to the {@link BankAccount}.
     *
     * @see BankAccount#addBankMember(UUID, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    ADD_MEMBERS,

    /**
     * Allows a player to remove members from the {@link BankAccount}.
     *
     * @see BankAccount#removeBankMember(UUID, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    REMOVE_MEMBERS,

    /**
     * Allows a player to add owners to the {@link BankAccount}.
     *
     * @see BankAccount#addBankOwner(UUID, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    ADD_OWNERS,

    /**
     * Allows a player to remove owners from the {@link BankAccount}.
     *
     * @see BankAccount#removeBankOwner(UUID, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    REMOVE_OWNERS
}