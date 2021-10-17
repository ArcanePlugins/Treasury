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

import me.lokka30.treasury.api.economy.response.EconomyPublisher;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

/**
 * @author lokka30, MrNemo64
 * @since v1.0.0
 * @see Account
 * A Bank Account is an Account which is associated with a Player
 * although separate from a PlayerAccount. It is used to store a
 * balance shared by multiple Bank Members, such as a 'town balance'
 * or 'faction balance'. The linked Player can be changed at any
 * time (Owning Player).
 */
@SuppressWarnings({"unused", "RedundantThrows"})
public interface BankAccount extends Account {

    /**
     * @author lokka30
     * @since v1.0.0
     * Get a list of each UUID of each member of this bank.
     * @return the list of UUIDs.
     */
    @NotNull
    EconomyPublisher<Collection<UUID>> getBankMembersIds();

    /**
     * @author lokka30
     * @since v1.0.0
     * Get a list of each UUID of each owner of this bank.
     * @return the list of UUIDs.
     */
    @NotNull
    EconomyPublisher<Collection<UUID>> getBankOwnersIds();

    /**
     * @author lokka30
     * @since v1.0.0
     * Check if the specified player is a member of the banks.
     * @param memberId is the uuid of the player to check.
     * @return whether the player is a member of the bank.
     */
    @NotNull
    EconomyPublisher<Boolean> isBankMember(@NotNull UUID memberId);

    /**
     * @author lokka30
     * @since v1.0.0
     * Check if the specified player is a member of the banks.
     * @param ownerId is the uuid of the player to check.
     * @return whether the player is a member of the bank.
     */
    @NotNull
    EconomyPublisher<Boolean> isBankOwner(@NotNull UUID ownerId);

    /**
     * @author lokka30
     * @since v1.0.0
     * Makes a player a member of the bank.
     * @param memberId is the uuid of the player to make a member of the bank.
     * @return whether the operation was successful.
     */
    @NotNull
    EconomyPublisher<Boolean> addBankMember(@NotNull UUID memberId);

    /**
     * @author lokka30
     * @since v1.0.0
     * Makes a player a owner of the bank.
     * @param ownerId is the uuid of the player to make a owner of the bank.
     * @return whether the operation was successful.
     */
    @NotNull
    EconomyPublisher<Boolean> addBankOwner(@NotNull UUID ownerId);

    /**
     * @author lokka30
     * @since v1.0.0
     * Makes a player no longer a member of the bank.
     * @param memberId is the uuid of the player to remove the member status of in the bank.
     * @return whether the operation was successful.
     */
    @NotNull
    EconomyPublisher<Boolean> removeBankMember(@NotNull UUID memberId);

    /**
     * @author lokka30
     * @since v1.0.0
     * Makes a player no longer a member of the bank.
     * @param ownerId is the uuid of the player to remove the owner status of in the bank.
     * @return whether the operation was successful.
     */
    @NotNull
    EconomyPublisher<Boolean> removeBankOwner(@NotNull UUID ownerId);
}
