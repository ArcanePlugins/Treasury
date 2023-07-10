/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common;

import java.util.UUID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a cause.
 * <p>A cause is a reason of why something got triggered.
 * <p>Causes are divided in 4 types: {@link Cause.Player}, {@link Cause.Plugin},
 * {@link Cause.NonPlayer} and {@link Cause#SERVER}. The server cause should only be used when
 * there is no other suitable cause, or there is not a possibility to create one.
 * <p>The raw {@code Cause} interface shall NOT be implemented, instead, if a custom cause
 * implementation is needed, please do so by implementing one of the causes already existing (e.g.
 * {@link Cause.Player}, {@link Cause.NonPlayer} or {@link Cause.Plugin}).
 *
 * @param <T> generic
 * @author MrIvanPlays
 * @since 2.0.0
 */
public interface Cause<T> {

    /**
     * Returns the server cause instance.
     */
    @NotNull Cause<String> SERVER = ServerCause.INSTANCE;

    /**
     * Creates a new player cause with the player {@link UUID unique id} as an identifier.
     *
     * @param uuid unique id of the player cause
     * @return new player cause
     */
    @Contract("_ -> new")
    @NotNull
    static Cause.Player player(@NotNull UUID uuid) {
        return () -> uuid;
    }

    /**
     * Creates a new plugin cause with a {@link NamespacedKey key} as a plugin identifier.
     *
     * @param id namespaced key plugin identifier
     * @return new plugin cause
     */
    @Contract("_ -> new")
    @NotNull
    static Cause.Plugin plugin(@NotNull NamespacedKey id) {
        return () -> id;
    }

    /**
     * Creates a new account cause with a {@link NamespacedKey key} as a non-player identifier.
     *
     * @param id namespaced key non-player identifier
     * @return new account cause
     */
    @Contract("_ -> new")
    @NotNull
    static Cause.NonPlayer nonPlayer(@NotNull NamespacedKey id) {
        return () -> id;
    }

    /**
     * Returns the identifier of the cause.
     *
     * @return identifier
     */
    @NotNull T identifier();

    /**
     * Check whether this {@code Cause} equals the provided cause.
     *
     * @param other other cause to check to
     * @return whether equals
     */
    boolean equals(@NotNull Cause<?> other);

    /**
     * Represents a player cause.
     *
     * @author MrIvanPlays
     * @since 2.0.0
     * @see Cause
     * @see Cause#player(UUID)
     */
    interface Player extends Cause<UUID> {

        /**
         * {@inheritDoc}
         */
        @Override
        default boolean equals(@NotNull Cause<?> other) {
            if (other instanceof Cause.Player) {
                return ((Cause.Player) other).identifier().equals(this.identifier());
            }
            if (other.identifier() instanceof UUID) {
                return ((UUID) other.identifier()).equals(this.identifier());
            }
            return false;
        }
    }

    /**
     * Represents a plugin cause.
     *
     * @author MrIvanPlays
     * @since 2.0.0
     * @see Cause
     * @see Cause#plugin(NamespacedKey)
     */
    interface Plugin extends Cause<NamespacedKey> {

        /**
         * {@inheritDoc}
         */
        @Override
        default boolean equals(@NotNull Cause<?> other) {
            if (other instanceof Cause.Plugin) {
                return ((Cause.Plugin) other).identifier().equals(this.identifier());
            }
            return false;
        }

    }

    /**
     * Represents a non-player cause.
     *
     * @author MrIvanPlays
     * @since 2.0.0
     * @see Cause
     * @see Cause#nonPlayer(NamespacedKey)
     */
    interface NonPlayer extends Cause<NamespacedKey> {

        /**
         * {@inheritDoc}
         */
        @Override
        default boolean equals(@NotNull Cause<?> other) {
            if (other instanceof Cause.NonPlayer) {
                return ((NonPlayer) other).identifier().equals(this.identifier());
            }
            return false;
        }

    }

}
