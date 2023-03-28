/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.account.accessor;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import org.jetbrains.annotations.NotNull;

/**
 * Provides access to {@link PlayerAccount player accounts}.
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
     * @return a resulting player account
     * @since 2.0.0
     */
    @NotNull
    public CompletableFuture<PlayerAccount> get() {
        return this.getOrCreate(new PlayerAccountCreateContext(Objects.requireNonNull(uniqueId,
                "uniqueId"
        )));
    }

    /**
     * Does the same as what {@link #get()} does, but returns a generic {@link Account}
     * interface, rather a specific {@link PlayerAccount} interface.
     *
     * @return a player account in the form of {@link Account}
     */
    @NotNull
    public CompletableFuture<Account> genericGet() {
        return this.get().thenApply(Function.identity());
    }

    @NotNull
    protected abstract CompletableFuture<PlayerAccount> getOrCreate(@NotNull PlayerAccountCreateContext context);

    /**
     * Represents a class, holder of data, needed to create/retrieve a {@link PlayerAccount}
     *
     * @author MrIvanPlays
     * @since 2.0.0
     */
    public static final class PlayerAccountCreateContext {

        private final UUID uuid;

        public PlayerAccountCreateContext(@NotNull UUID uuid) {
            this.uuid = uuid;
        }

        /**
         * Returns the {@link UUID unique identifier} of the {@link PlayerAccount player account}
         * created/retrieved.
         *
         * @return unique id
         */
        @NotNull
        public UUID getUniqueId() {
            return uuid;
        }

    }

}
