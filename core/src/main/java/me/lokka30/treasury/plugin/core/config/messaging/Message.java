package me.lokka30.treasury.plugin.core.config.messaging;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a message, holding a {@link MessageKey} and potential {@link MessagePlaceholder placeholders}.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public final class Message {

    @NotNull
    public static Message of(@NotNull MessageKey messageKey, @Nullable MessagePlaceholder... placeholders) {
        return new Message(messageKey, placeholders);
    }

    private final MessageKey key;
    private final MessagePlaceholder[] placeholders;

    private Message(@NotNull MessageKey key, @Nullable MessagePlaceholder... placeholders) {
        this.key = key;
        this.placeholders = placeholders;
    }

    @NotNull
    public MessageKey getKey() {
        return key;
    }

    /**
     * Replaces the placeholders onto the given message, if any specified.
     *
     * @param message message
     * @return message with replaced placeholders
     * @author MrIvanPlays
     * @since v1.0.0
     */
    @NotNull
    public String handlePlaceholders(@NotNull String message) {
        Objects.requireNonNull(message, "message");
        // todo: special handling for the %prefix% placeholder
        if (placeholders == null) {
            return message;
        }
        for (MessagePlaceholder placeholder : placeholders) {
            message = message.replaceAll(
                    String.valueOf(placeholder.getToReplace()),
                    String.valueOf(placeholder.getReplacement())
            );
        }
        return message;
    }
}
