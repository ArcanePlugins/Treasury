/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.core.config.messaging;

import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
