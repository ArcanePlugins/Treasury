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
 * A Generic account is any non-player account.
 *
 * @author lokka30, MrNemo64
 * @see Account
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface GenericAccount extends Account {

    /**
     * Request a listing of all member players of the account.
     *
     * @param subscription the {@link EconomySubscriber} accepting the members
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveMemberIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Check if the specified user is a member of the account.
     *
     * <p>A member is any player with at least one allowed permission.
     *
     * @param player     the {@link UUID} of the potential member
     * @param subscription the {@link EconomySubscriber} accepting whether the user is a member
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void isMember(@NotNull UUID player, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Modifies the state of the specified {@link AccountPermission} {@code permissions} for the specified {@link UUID}
     * {@code player}. The state is specified via the {@link TriState} {@code permissionValue}. If
     * {@link TriState#UNSPECIFIED} is specified for a {@code permissionValue}, then the specified {@code permissions} get
     * unbound from the specified {@code player}. If {@link TriState#FALSE} is specified for a {@code permissionValue}, then
     * the specified {@code permissions} become forbidden for the {@code player}, no matter whether they had them in the past.
     *
     * <p>Just a reminder: a member is any player with at least one allowed permission.
     *
     * @param player        the player id you want to modify the permissions of
     * @param permissionValue the permission value you want to set
     * @param subscription    the {@link EconomySubscriber} accepting whether the permissions were removed or not
     * @param permissions     the permissions to modify
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void setPermission(
            @NotNull UUID player,
            @NotNull TriState permissionValue,
            @NotNull EconomySubscriber<Boolean> subscription,
            @NotNull AccountPermission @NotNull ... permissions
    );

    /**
     * Request the {@link AccountPermission AccountPermissions} for the specified {@link UUID} {@code player}
     *
     * @param player     the player {@link UUID} to get the permissions for
     * @param subscription the {@link EconomySubscriber} accepting an immutable map of permissions and their values.
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrievePermissions(
            @NotNull UUID player, @NotNull EconomySubscriber<Map<AccountPermission, TriState>> subscription
    );

    /**
     * Checks whether given player has the given permission on this account.
     *
     * <p>Just a reminder: a member is any player with at least one allowed permission.
     *
     * @param player       the {@link UUID} of the player to check if they have the permission
     * @param subscription the {@link EconomySubscriber} accepting whether the player has all the specified permissions
     * @param permissions  the permissions to check
     * @author MrNemo64, MrIvanPlays
     * @see AccountPermission
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void hasPermission(
            @NotNull UUID player,
            @NotNull EconomySubscriber<Boolean> subscription,
            @NotNull AccountPermission @NotNull ... permissions
    );
}
