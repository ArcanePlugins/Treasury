/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.account.accessor;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import me.lokka30.treasury.api.common.NamespacedKey;
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

    private String name;
    private NamespacedKey identifier;

    protected NonPlayerAccountAccessor() {
    }

    /**
     * Specifies the {@link NamespacedKey} identifier of the accessed/created
     * {@link NonPlayerAccount non player account}
     *
     * @param identifier account identifier
     * @return this instance for chaining
     * @since 2.0.0
     */
    @NotNull
    public NonPlayerAccountAccessor withIdentifier(@NotNull NamespacedKey identifier) {
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
     * @return a resulting non player account
     */
    @NotNull
    public CompletableFuture<NonPlayerAccount> get() {
        return this.getOrCreate(new NonPlayerAccountCreateContext(Objects.requireNonNull(identifier,
                "identifier"
        ), name));
    }

    /**
     * Does the same as what {@link #get()} does, but returns a generic {@link Account}
     * interface, rather a specific {@link NonPlayerAccount} interface.
     *
     * @return a non player account in the form of {@link Account}
     */
    @NotNull
    public CompletableFuture<Account> genericGet() {
        return this.get().thenApply(Function.identity());
    }

    @NotNull
    protected abstract CompletableFuture<NonPlayerAccount> getOrCreate(
            @NotNull NonPlayerAccountCreateContext context
    );

    /**
     * Represents a class, holder of data, needed to create/retrieve a {@link NonPlayerAccount}
     *
     * @author MrIvanPlays
     * @since 2.0.0
     */
    public static final class NonPlayerAccountCreateContext {

        private final String name;
        private final NamespacedKey identifier;

        public NonPlayerAccountCreateContext(
                @NotNull NamespacedKey identifier, @Nullable String name
        ) {
            this.identifier = identifier;
            this.name = name;
        }

        /**
         * Returns the {@link NamespacedKey identifier} of the {@link NonPlayerAccount non player
         * account}
         * created/retrieved.
         *
         * @return string identifier
         */
        @NotNull
        public NamespacedKey getIdentifier() {
            return identifier;
        }

        /**
         * Returns the (new) name of the {@link NonPlayerAccount non player account} created.
         * <p><b>WARNING:</b> Names are not identifiers of non player accounts. The
         * {@link #getIdentifier()} is.
         *
         * @return name
         */
        @Nullable
        public String getName() {
            return name;
        }

    }

}
