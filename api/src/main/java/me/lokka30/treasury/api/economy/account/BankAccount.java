/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
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
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface BankAccount extends Account {

    /**
     * Request a listing of all members of the bank.
     *
     * @param subscription the {@link EconomySubscriber} accepting the members
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveBankMembersIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request a listing of all owners of the bank.
     *
     * @param subscription the {@link EconomySubscriber} accepting the owners
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveBankOwnersIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Check if the specified user is a member of the bank.
     *
     * @param memberId the {@link UUID} of the potential member
     * @param subscription the {@link EconomySubscriber} accepting whether the user is a member
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void isBankMember(@NotNull UUID memberId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Check if the specified user is an owner of the bank.
     *
     * @param ownerId the {@link UUID} of the potential owner
     * @param subscription the {@link EconomySubscriber} accepting whether the user is an owner
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void isBankOwner(@NotNull UUID ownerId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Make a user a member of the bank.
     *
     * @param memberId the {@link UUID} of the new member
     * @param subscription the {@link EconomySubscriber} accepting whether the member was added
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void addBankMember(@NotNull UUID memberId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Make a user an owner of the bank.
     *
     * @param ownerId the {@link UUID} of the potential new owner
     * @param subscription the {@link EconomySubscriber} accepting whether the owner was added
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void addBankOwner(@NotNull UUID ownerId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Remove a member of the bank.
     *
     * @param memberId the {@link UUID} of the member removed
     * @param subscription the {@link EconomySubscriber} accepting whether the member was removed
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void removeBankMember(@NotNull UUID memberId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Remove an owner of the bank.
     *
     * @param ownerId the {@link UUID} of the owner removed
     * @param subscription the {@link EconomySubscriber} accepting whether the owner was removed
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void removeBankOwner(@NotNull UUID ownerId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Checks whether given player has the given permission on this bank account.
     *
     * @param player the {@link UUID} of the player to check if they have the permission
     * @param permission the permission to check
     * @param subscription the {@link EconomySubscriber} accepting whether the player has the permission
     * @author MrNemo64
     * @see BankAccountPermission
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void hasPermission(@NotNull UUID player, @NotNull BankAccountPermission permission, @NotNull EconomySubscriber<Boolean> subscription);

}