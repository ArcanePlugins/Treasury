/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account.accessor;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides access to {@link NonPlayerAccount non-player accounts}.
 *
 * @author MrIvanPlays
 * @since 2.0.0
 */
public abstract class NonPlayerAccountAccessor {

    private String identifier, name;

    protected NonPlayerAccountAccessor() {
    }

    /**
     * Specifies the identifier of the accessed/created {@link NonPlayerAccount non player account}
     *
     * @param identifier account identifier
     * @return this instance for chaining
     * @since 2.0.0
     */
    @NotNull
    public NonPlayerAccountAccessor withIdentifier(@NotNull String identifier) {
        this.identifier = Objects.requireNonNull(identifier, "identifier");
        return this;
    }

    /**
     * Specifies the name of the accessed/created {@link NonPlayerAccount non-player account}.
     * This is optional to set.
     *
     * @param name account name
     * @return this instance for chaining
     * @since 2.0.0
     */
    @NotNull
    public NonPlayerAccountAccessor withName(@Nullable String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets or creates the {@link NonPlayerAccount non player account} needed.
     *
     * @return future with {@link Response} which if successful returns the resulting
     *         {@link NonPlayerAccount}
     */
    @NotNull
    public CompletableFuture<Response<NonPlayerAccount>> get() {
        return this.getOrCreate(Objects.requireNonNull(identifier, "identifier"), name);
    }

    /**
     * Does the same as what {@link #get()} does, but returns a generic {@link Account}
     * interface, rather a specific {@link NonPlayerAccount} interface.
     *
     * @return future with response which if successful returns the resulting
     *         {@link NonPlayerAccount} in the form of {@link Account}
     */
    @NotNull
    public CompletableFuture<Response<Account>> genericGet() {
        return this.get().thenApply(resp -> {
            if (!resp.isSuccessful()) {
                return Response.failure(resp.getFailureReason());
            }
            return Response.success(resp.getResult());
        });
    }

    @NotNull
    protected abstract CompletableFuture<Response<NonPlayerAccount>> getOrCreate(
            @NotNull String identifier,
            @Nullable String name
    );

}
