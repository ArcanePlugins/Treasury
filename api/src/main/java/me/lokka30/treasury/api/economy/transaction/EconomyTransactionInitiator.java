package me.lokka30.treasury.api.economy.transaction;

import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an initiator of {@link EconomyTransaction}.
 * <p>Initiator is something/someone, triggering a transaction.
 * <p>Initiators are divided in 3 types: {@link Type#PLAYER}, {@link Type#PLUGIN} and {@link Type#SERVER}.
 * The PLAYER and PLUGIN types should always provide data which matches types:
 * <ul>
 *     <li>For PLAYER type: data type should be an {@link UUID} of the player, whom initiated the transaction</li>
 *     <li>For PLUGIN type: data type should be am {@link String}, fulfilled with the plugin name, whom initiated the
 *     transaction.</li>
 * </ul>
 * <p>In most of the cases, if the initiator is of type SERVER, {@link EconomyTransactionInitiator#SERVER} is going to be fine,
 * as the SERVER type does not require any data to be held. If though data is needed, then creation of initiators with SERVER
 * type is permitted.
 * <p>It is highly recommended creating a constant somewhere in plugins, which are utilising Treasury API and are not economy
 * providers, which shall be a PLUGIN initiator.
 * <p>Example usage of an Economy initiator inside a method in {@link me.lokka30.treasury.api.economy.account.Account}
 * <pre>{@code
 *     Account account = // ...
 *     account.depositBalance(
 *         20,
 *         EconomyTransactionInitiator.createInitiator(Type.PLAYER, playerInitiator.getUniqueId()),
 *         // ...
 *     );
 *
 * }</pre>
 * <p>Example usage of an Economy initiator whenever retrieved from a {@link EconomyTransaction}
 * <pre>{@code
 *     EconomyTransaction transaction = // ...
 *     EconomyTransactionInitiator<?> initiator = transaction.getInitiator();
 *     if (initiator.getType() == Type.PLUGIN) {
 *         String pluginName = (String) initiator.getData();
 *     } else if (initiator.getType() == Type.PLAYER) {
 *         UUID playerInitiator = (UUID) initiator.getData();
 *     }
 *     // ...
 * }</pre>
 *
 * @param <T> generic
 * @author MrIvanPlays
 */
public interface EconomyTransactionInitiator<T> {

    /**
     * Returns an {@code EconomyTransactionInitiator} with {@link Type#SERVER} type.
     */
    EconomyTransactionInitiator<?> SERVER = new EconomyTransactionInitiator<Object>() {
        @Override
        public Object getData() {
            return null;
        }

        @Override
        public @NotNull Type getType() {
            return Type.SERVER;
        }
    };

    /**
     * Creates a new {@link EconomyTransactionInitiator}
     *
     * @param type initiator type
     * @param data initiator data
     * @param <T>  generic
     * @return new economy transaction initiator
     */
    @NotNull
    static <T> EconomyTransactionInitiator<T> createInitiator(@NotNull Type type, T data) {
        Objects.requireNonNull(type, "type");
        switch (type) {
            case PLAYER:
                if (data == null) {
                    throw new IllegalArgumentException("Player initiator with no data");
                }
                if (!(data instanceof UUID)) {
                    throw new IllegalArgumentException(
                            "Player initiator with data which is not an UUID");
                }
                break;
            case PLUGIN:
                if (data == null) {
                    throw new IllegalArgumentException("Plugin initiator with no data");
                }
                if (!(data instanceof String)) {
                    throw new IllegalArgumentException(
                            "Plugin initiator with data which is not an String");
                }
                break;
            case SERVER:
                if (data == null) {
                    return (EconomyTransactionInitiator<T>) SERVER;
                }
                break;
            default:
                break;
        }
        return new EconomyTransactionInitiator<T>() {
            @Override
            public T getData() {
                return data;
            }

            @Override
            public @NotNull Type getType() {
                return type;
            }
        };
    }

    /**
     * Represents a {@link EconomyTransactionInitiator} type.
     *
     * @author MrIvanPlays
     */
    enum Type {
        PLAYER,
        PLUGIN,
        SERVER;
    }

    /**
     * Get the data, held in this {@code EconomyTransactionInitiator}
     *
     * @return data
     */
    T getData();

    /**
     * Get the {@link Type} for this {@code EconomyTransactionInitiator}
     *
     * @return type
     */
    @NotNull Type getType();

}
