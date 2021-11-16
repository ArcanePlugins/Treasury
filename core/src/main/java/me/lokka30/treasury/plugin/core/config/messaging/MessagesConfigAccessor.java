package me.lokka30.treasury.plugin.core.config.messaging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an accessor of messages.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public interface MessagesConfigAccessor {

    MessagesConfigAccessor EMPTY = new MessagesConfigAccessor() {
        @Override
        public @Nullable Object getMessage(@NotNull String key) {
            return null;
        }

        @Override
        public boolean justGenerated() {
            return true;
        }
    };

    /**
     * Returns the value message bound to the specified key.
     *
     * @param key the key you want the value for
     * @return value or null
     */
    @Nullable
    Object getMessage(@NotNull String key);

    /**
     * Returns whether the messages have been just generated so the plugin doesn't complain
     * to admins that the value is missing
     *
     * @return just generated or not
     */
    default boolean justGenerated() {
        return false;
    }
}
