/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.misc.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Returns the name of this {@code BankAccount}, if specified. Empty optional otherwise.
     *
     * <p>A economy provider may choose not to provide a name.
     *
     * @return an optional fulfilled with a name or an empty optional
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    Optional<String> getName();

    /**
     * Sets a new name for this {@code BankAccount}, which may be null.
     *
     * @param name         the new name for this bank account.
     * @param subscription the {@link EconomySubscriber} accepting whether name change was successful
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void setName(@Nullable String name, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Request a listing of all members of the bank.
     *
     * @param subscription the {@link EconomySubscriber} accepting the members
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveBankMembersIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Check if the specified user is a member of the bank.
     *
     * <p>A Bank member is any player with at least one allowed permission.
     *
     * @param memberId     the {@link UUID} of the potential member
     * @param subscription the {@link EconomySubscriber} accepting whether the user is a member
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void isBankMember(@NotNull UUID memberId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Modifies the state of the specified {@link BankAccountPermission} {@code permissions} for the specified {@link UUID}
     * {@code memberId}. The state is specified via the {@link TriState} {@code permissionValue}. If
     * {@link TriState#UNSPECIFIED} is specified for a {@code permissionValue}, then the specified {@code permissions} get
     * unbound from the specified {@code memberId}. If {@link TriState#FALSE} is specified for a {@code permissionValue}, then
     * the specified {@code permissions} become forbidden for the {@code memberId}, no matter whether they had them in the past.
     *
     * <p>Just a reminder: a Bank member is any player with at least one allowed permission.
     *
     * @param memberId        the member id you want to modify the permissions of
     * @param permissionValue the permission value you want to set
     * @param subscription    the {@link EconomySubscriber} accepting whether the permissions were removed or not
     * @param permissions     the permissions to modify
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void setPermission(
            @NotNull UUID memberId,
            @NotNull TriState permissionValue,
            @NotNull EconomySubscriber<Boolean> subscription,
            @NotNull BankAccountPermission @NotNull ... permissions
    );

    /**
     * Request the {@link BankAccountPermission BankAccountPermissions} for the specified {@link UUID} {@code memberId}
     *
     * @param memberId     the member id to get the permissions for
     * @param subscription the {@link EconomySubscriber} accepting an immutable map of permissions and their values.
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrievePermissions(
            @NotNull UUID memberId, @NotNull EconomySubscriber<Map<BankAccountPermission, TriState>> subscription
    );

    /**
     * Checks whether given player has the given permission on this bank account.
     *
     * <p>Just a reminder: a Bank member is any player with at least one allowed permission.
     *
     * @param player       the {@link UUID} of the player to check if they have the permission
     * @param subscription the {@link EconomySubscriber} accepting whether the player has all the specified permissions
     * @param permissions  the permissions to check
     * @author MrNemo64, MrIvanPlays
     * @see BankAccountPermission
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void hasPermission(
            @NotNull UUID player,
            @NotNull EconomySubscriber<Boolean> subscription,
            @NotNull BankAccountPermission @NotNull ... permissions
    );

}
