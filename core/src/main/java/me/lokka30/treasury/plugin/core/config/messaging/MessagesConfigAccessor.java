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

    static MessagesConfigAccessor empty() {
        return (key) -> null;
    }

    /**
     * Returns the value message bound to the specified key.
     *
     * @param key the key you want the value for
     * @return value or null
     */
    @Nullable
    Object getMessage(@NotNull String key);
}
