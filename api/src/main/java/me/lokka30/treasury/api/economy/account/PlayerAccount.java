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
import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.api.common.misc.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code PlayerAccount} is an {@link Account account} owned by a Player. Economy providers are
 * likely to create player accounts for players when they join the server, although this is
 * optional.
 * <br>
 * A Player, on all the platforms Treasury plugin aims to support, is described as a minecraft
 * client, mainly identifiable by a {@link #identifier() unique-id}.
 *
 * @author lokka30, Geolykt, creatorfromhell
 * @see Account
 * @since v1.0.0
 */
public interface PlayerAccount extends Account, Cause.Player {

    /**
     * Returns a map fulfilled with all {@link AccountPermission} with {@link TriState} values of
     * {@link TriState#TRUE}.
     */
    Map<AccountPermission, TriState> ALL_PERMISSIONS_MAP = Collections.unmodifiableMap(Arrays
            .stream(AccountPermission.values())
            .collect(Collectors.toConcurrentMap(p -> p, $ -> TriState.TRUE)));

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Boolean> setName(@Nullable String name) {
        return CompletableFuture.completedFuture(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Boolean> isMember(@NotNull UUID player) {
        Objects.requireNonNull(player, "player");
        return CompletableFuture.completedFuture(this.identifier().equals(player));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Collection<UUID>> retrieveMemberIds() {
        return CompletableFuture.completedFuture(Collections.singletonList(this.identifier()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<TriState> hasPermissions(
            @NotNull UUID player, @NotNull AccountPermission @NotNull ... permissions
    ) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(permissions, "permissions");

        return CompletableFuture.completedFuture(TriState.fromBoolean(this.identifier().equals(player)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Map<AccountPermission, TriState>> retrievePermissions(
            @NotNull UUID player
    ) {
        Objects.requireNonNull(player, "player");

        return CompletableFuture.completedFuture(this.identifier().equals(player)
                ? ALL_PERMISSIONS_MAP
                : Collections.emptyMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Map<UUID, Map<AccountPermission, TriState>>> retrievePermissionsMap() {
        return CompletableFuture.completedFuture(Collections.singletonMap(
                this.identifier(),
                ALL_PERMISSIONS_MAP
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default CompletableFuture<Boolean> setPermissions(
            @NotNull UUID player, @NotNull Map<AccountPermission, TriState> permissionsMap
    ) {
        Objects.requireNonNull(player, "player");

        return CompletableFuture.completedFuture(false);
    }

}
