/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code PlayerAccount} is an {@link Account account} owned by a Player. Economy providers are
 * likely to create player accounts for players when they join the server, although this is
 * optional.
 * <br>
 * A Player, on all the platforms Treasury plugin aims to support, is described as a minecraft
 * client, mainly identifiable by a {@link #getUniqueId() unique-id}.
 *
 * @author lokka30, Geolykt, creatorfromhell
 * @see Account
 * @since v1.0.0
 */
public interface PlayerAccount extends Account {

    /**
     * Returns a map fulfilled with all {@link AccountPermission} with {@link TriState} values of
     * {@link TriState#TRUE}.
     */
    Map<AccountPermission, TriState> ALL_PERMISSIONS_MAP = Collections.unmodifiableMap(Arrays
            .stream(AccountPermission.values())
            .collect(Collectors.toConcurrentMap(p -> p, $ -> TriState.TRUE)));

    /**
     * A {@link FailureReason}, describing that for {@code PlayerAccounts}, modifying the
     * permissions are not supported.
     */
    FailureReason PLAYER_ACCOUNT_PERMISSION_MODIFICATION_NOT_SUPPORTED = () -> "Cannot modify the permissions of a player account!";

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Response<TriState>> setName(@Nullable String name) {
        return CompletableFuture.completedFuture(Response.success(TriState.FALSE));
    }

    /**
     * Get the {@link UUID unique identifier} of this {@code PlayerAccount}.
     *
     * @return account identifier
     * @see UUID
     * @since v1.0.0
     */
    @NotNull UUID getUniqueId();

    /**
     * Get this {@code PlayerAccount} as a {@link EconomyTransactionInitiator transaction
     * initiator}.
     * <p>
     * The return value of this method shall be cached upon a {@code PlayerAccount} creation.
     *
     * @return this player account, represented by an economy transaction initiator
     */
    @NotNull EconomyTransactionInitiator<UUID> getAsTransactionInitiator();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Response<Boolean>> isMember(@NotNull UUID player) {
        Objects.requireNonNull(player, "player");
        return CompletableFuture.completedFuture(Response.success(getUniqueId().equals(player)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Response<Collection<UUID>>> retrieveMemberIds() {
        return CompletableFuture.completedFuture(Response.success(Collections.singletonList(
                getUniqueId())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Response<TriState>> hasPermissions(
            @NotNull UUID player, @NotNull AccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(permissions, "permissions");

        return CompletableFuture.completedFuture(Response.success(TriState.fromBoolean(getUniqueId().equals(
                player))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Response<Map<AccountPermission, TriState>>> retrievePermissions(
            @NotNull UUID player
    ) {
        Objects.requireNonNull(player, "player");

        return CompletableFuture.completedFuture(Response.success(getUniqueId().equals(player)
                ? ALL_PERMISSIONS_MAP
                : Collections.emptyMap()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Response<Map<UUID, Map<AccountPermission, TriState>>>> retrievePermissionsMap() {
        return CompletableFuture.completedFuture(Response.success(Collections.singletonMap(
                this.getUniqueId(),
                ALL_PERMISSIONS_MAP
        )));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Response<TriState>> setPermissions(
            @NotNull UUID player, @NotNull Map<AccountPermission, TriState> permissionsMap
    ) {
        Objects.requireNonNull(player, "player");

        return CompletableFuture.completedFuture(Response.failure(
                PLAYER_ACCOUNT_PERMISSION_MODIFICATION_NOT_SUPPORTED));
    }

}
