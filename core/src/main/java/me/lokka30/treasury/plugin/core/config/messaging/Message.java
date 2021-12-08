/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.config.messaging;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
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
        if (placeholders == null) {
            return message.replaceAll(
                    "%prefix%",
                    TreasuryPlugin.getInstance().configAdapter().getMessages().getSingleMessage(MessageKey.PREFIX)
            );
        }
        boolean prefixHandled = false;
        for (MessagePlaceholder placeholder : placeholders) {
            String toReplace = String.valueOf(placeholder.getToReplace());
            if (!toReplace.startsWith("%")) {
                toReplace = "%" + toReplace;
            }
            if (!toReplace.endsWith("%")) {
                toReplace = toReplace + "%";
            }
            message = message.replaceAll(toReplace, String.valueOf(placeholder.getReplacement()));
            if (toReplace.equalsIgnoreCase("%prefix%")) {
                prefixHandled = true;
            }
        }
        if (!prefixHandled) {
            message = message.replaceAll(
                    "%prefix%",
                    TreasuryPlugin.getInstance().configAdapter().getMessages().getSingleMessage(MessageKey.PREFIX)
            );
        }
        return message;
    }

    /**
     * Replaces the placeholders onto the given messages, if any specified.
     *
     * @param message the messages to handle placeholders
     * @return messages with replaced placeholders
     * @author MrIvanPlays
     * @since v1.0.0
     */
    @NotNull
    public List<String> handlePlaceholders(@NotNull List<String> message) {
        Objects.requireNonNull(message, "message");
        if (message.isEmpty()) {
            return message;
        }
        if (message.size() == 1) {
            return Collections.singletonList(handlePlaceholders(message.get(0)));
        }
        return message.stream().map(this::handlePlaceholders).collect(Collectors.toList());
    }
}
