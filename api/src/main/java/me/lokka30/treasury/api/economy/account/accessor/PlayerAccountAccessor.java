/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account.accessor;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an account accessor, which accesses {@link PlayerAccount player accounts}
 *
 * @author MrIvanPlays
 * @since 2.0.0
 */
public abstract class PlayerAccountAccessor {

    private UUID uniqueId;

    protected PlayerAccountAccessor() {
    }

    /**
     * Specifies the {@link UUID unique id} of the account owner
     *
     * @param uniqueId account owner uuid
     * @return this instance for chaining
     * @since 2.0.0
     */
    @NotNull
    public PlayerAccountAccessor withUniqueId(@NotNull UUID uniqueId) {
        this.uniqueId = Objects.requireNonNull(uniqueId, "uniqueId");
        return this;
    }

    /**
     * Gets or creates the {@link PlayerAccount player account} needed.
     *
     * @return future with {@link Response} which if successful returns the resulting
     *         {@link PlayerAccount}
     * @since 2.0.0
     */
    @NotNull
    public CompletableFuture<Response<PlayerAccount>> get() {
        return this.getOrCreate(uniqueId);
    }

    @NotNull
    protected abstract CompletableFuture<Response<PlayerAccount>> getOrCreate(@NotNull UUID uniqueId);

}
