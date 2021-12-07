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

import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

/**
 * A Bank Account is an Account which is associated with a Player
 * although separate from a PlayerAccount. It is used to store a
 * balance shared by multiple Bank Members, such as a 'town balance'
 * or 'faction balance'. The linked Player can be changed at any
 * time (Owning Player).
 * @author lokka30, MrNemo64
 * @see Account
 * @since v1.0.0
 */
@SuppressWarnings({ "unused" })
public interface BankAccount extends Account {

    /**
     * Request a listing of all members of the bank.
     * @param subscription the {@link EconomySubscriber} accepting the members
     * @author lokka30
     * @since v1.0.0
     */
    void retrieveBankMembersIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request a listing of all owners of the bank.
     * @param subscription the {@link EconomySubscriber} accepting the owners
     * @author lokka30
     * @since v1.0.0
     */
    void retrieveBankOwnersIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Check if the specified user is a member of the bank.
     * @param memberId the {@link UUID} of the potential member
     * @param subscription the {@link EconomySubscriber} accepting whether the user is a member
     * @author lokka30
     * @since v1.0.0
     */
    void isBankMember(@NotNull UUID memberId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Check if the specified user is an owner of the bank.
     * @param ownerId the {@link UUID} of the potential owner
     * @param subscription the {@link EconomySubscriber} accepting whether the user is an owner
     * @author lokka30
     * @since v1.0.0
     */
    void isBankOwner(@NotNull UUID ownerId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Make a user a member of the bank.
     * @param memberId the {@link UUID} of the new member
     * @param subscription the {@link EconomySubscriber} accepting whether the member was added
     * @author lokka30
     * @since v1.0.0
     */
    void addBankMember(@NotNull UUID memberId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Make a user an owner of the bank.
     * @param ownerId the {@link UUID} of the potential new owner
     * @param subscription the {@link EconomySubscriber} accepting whether the owner was added
     * @author lokka30
     * @since v1.0.0
     */
    void addBankOwner(@NotNull UUID ownerId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Remove a member of the bank.
     * @param memberId the {@link UUID} of the member removed
     * @param subscription the {@link EconomySubscriber} accepting whether the member was removed
     * @author lokka30
     * @since v1.0.0
     */
    void removeBankMember(@NotNull UUID memberId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Remove an owner of the bank.
     * @param ownerId the {@link UUID} of the owner removed
     * @param subscription the {@link EconomySubscriber} accepting whether the owner was removed
     * @author lokka30
     * @since v1.0.0
     */
    void removeBankOwner(@NotNull UUID ownerId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Checks whether given player has the given permission on this bank account.
     * @param player the {@link UUID} of the player to check if they have the permission
     * @param permission the permission to check
     * @param subscription the {@link EconomySubscriber} accepting whether the player has the permission
     * @author MrNemo64
     * @see #canConsultBalance(UUID, EconomySubscriber) 
     * @see #canWithdraw(UUID, EconomySubscriber) 
     * @see #canDeposit(UUID, EconomySubscriber)
     * @see BankAccountPermission
     * @since v1.0.0
     */
    void hasPermission(@NotNull UUID player, @NotNull BankAccountPermission permission, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Checks whether the given player can consult the balance of this bank account,
     * in other words, if they have the {@link BankAccountPermission#CONSULT} permission.
     * @param player the {@link UUID} of the player to check if they can consult the balance
     * @param subscription the {@link EconomySubscriber} accepting whether the player can consult the balance
     * @author MrNemo64
     * @see #hasPermission(UUID, BankAccountPermission, EconomySubscriber)
     * @see BankAccountPermission#CONSULT
     * @since v1.0.0
     */
    default void canConsultBalance(@NotNull UUID player, @NotNull EconomySubscriber<Boolean> subscription) {
        hasPermission(player, BankAccountPermission.CONSULT, subscription);
    }

    /**
     * Checks whether the given player can withdraw from this bank account,
     * in other words, if they have the {@link BankAccountPermission#WITHDRAW} permission.
     * @param player the {@link UUID} of the player to check if they can withdraw
     * @param subscription the {@link EconomySubscriber} accepting whether the player can withdraw
     * @author MrNemo64
     * @see #hasPermission(UUID, BankAccountPermission, EconomySubscriber)
     * @see BankAccountPermission#WITHDRAW
     * @since v1.0.0
     */
    default void canWithdraw(@NotNull UUID player, @NotNull EconomySubscriber<Boolean> subscription) {
        hasPermission(player, BankAccountPermission.WITHDRAW, subscription);
    }

    /**
     * Checks whether the given player can deposit on this bank account,
     * in other words, if they have the {@link BankAccountPermission#DEPOSIT} permission.
     * @param player the {@link UUID} of the player to check if they can deposit
     * @param subscription the {@link EconomySubscriber} accepting whether the player can deposit
     * @author MrNemo64
     * @see #hasPermission(UUID, BankAccountPermission, EconomySubscriber)
     * @see BankAccountPermission#DEPOSIT
     * @since v1.0.0
     */
    default void canDeposit(@NotNull UUID player, @NotNull EconomySubscriber<Boolean> subscription) {
        hasPermission(player, BankAccountPermission.DEPOSIT, subscription);
    }

}
