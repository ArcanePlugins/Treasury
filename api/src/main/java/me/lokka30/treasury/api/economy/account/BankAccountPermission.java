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
import me.lokka30.treasury.api.economy.response.EconomySubscriber;

import java.util.UUID;

/**
 * Enum that holds the permissions of a {@link BankAccount}.
 * @author MrNemo64
 * @see BankAccount
 * @since v1.0.0
 */
public enum BankAccountPermission {

    /**
     * Allows a player to consult the balance of the {@link BankAccount}.
     * @see Account#retrieveBalance(Currency, EconomySubscriber)
     * @since v1.0.0
     */
    CONSULT,
    /**
     * Allows a player to withdraw from the {@link BankAccount}.
     * @see Account#withdrawBalance(double, Currency, EconomySubscriber)
     * @since v1.0.0
     */
    WITHDRAW,
    /**
     * Allows a player to deposit on the {@link BankAccount}.
     * @see Account#depositBalance(double, Currency, EconomySubscriber)
     * @since v1.0.0
     */
    DEPOSIT,
    /**
     * Allows a player to add members to the {@link BankAccount}.
     * @see BankAccount#addBankMember(UUID, EconomySubscriber)
     * @since v1.0.0
     */
    ADD_MEMBERS,
    /**
     * Allows a player to remove members from the {@link BankAccount}.
     * @see BankAccount#removeBankMember(UUID, EconomySubscriber)
     * @since v1.0.0
     */
    REMOVE_MEMBERS,
    /**
     * Allows a player to add owners to the {@link BankAccount}.
     * @see BankAccount#addBankOwner(UUID, EconomySubscriber)
     * @since v1.0.0
     */
    ADD_OWNERS,
    /**
     * Allows a player to remove owners from the {@link BankAccount}.
     * @see BankAccount#removeBankOwner(UUID, EconomySubscriber)
     * @since v1.0.0
     */
    REMOVE_OWNERS;
}