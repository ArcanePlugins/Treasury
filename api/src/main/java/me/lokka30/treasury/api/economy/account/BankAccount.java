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
 *
 * @author lokka30, MrNemo64
 * @see Account
 * @since v1.0.0
 */
@SuppressWarnings({"unused"})
public interface BankAccount extends Account {

    /**
     * Request a listing of all members of the bank.
     *
     * @author lokka30
     * @param subscription the {@link EconomySubscriber} accepting the members
     * @since v1.0.0
     */
    void retrieveBankMembersIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request a listing of all owners of the bank.
     *
     * @author lokka30
     * @param subscription the {@link EconomySubscriber} accepting the owners
     * @since v1.0.0
     */
    void retrieveBankOwnersIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Check if the specified user is a member of the bank.
     *
     * @author lokka30
     * @param memberId the {@link UUID} of the potential member
     * @param subscription the {@link EconomySubscriber} accepting whether the user is a member
     * @since v1.0.0
     */
    void isBankMember(@NotNull UUID memberId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Check if the specified user is an owner of the bank.
     *
     * @author lokka30
     * @param ownerId the {@link UUID} of the potential owner
     * @param subscription the {@link EconomySubscriber} accepting whether the user is an owner
     * @since v1.0.0
     */
    void isBankOwner(@NotNull UUID ownerId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Make a user a member of the bank.
     *
     * @author lokka30
     * @param memberId the {@link UUID} of the new member
     * @param subscription the {@link EconomySubscriber} accepting whether the member was added
     * @since v1.0.0
     */
    void addBankMember(@NotNull UUID memberId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Make a user an owner of the bank.
     *
     * @author lokka30
     * @param ownerId the {@link UUID} of the potential new owner
     * @param subscription the {@link EconomySubscriber} accepting whether the owner was added
     * @since v1.0.0
     */
    void addBankOwner(@NotNull UUID ownerId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Remove a member of the bank.
     *
     * @author lokka30
     * @param memberId the {@link UUID} of the member removed
     * @param subscription the {@link EconomySubscriber} accepting whether the member was removed
     * @since v1.0.0
     */
    void removeBankMember(@NotNull UUID memberId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Remove an owner of the bank.
     *
     * @author lokka30
     * @param ownerId the {@link UUID} of the owner removed
     * @param subscription the {@link EconomySubscriber} accepting whether the owner was removed
     * @since v1.0.0
     */
    void removeBankOwner(@NotNull UUID ownerId, @NotNull EconomySubscriber<Boolean> subscription);
}
