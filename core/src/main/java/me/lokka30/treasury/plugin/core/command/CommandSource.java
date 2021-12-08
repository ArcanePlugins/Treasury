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

package me.lokka30.treasury.plugin.core.command;

import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents an executor of a command
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public interface CommandSource {

    /**
     * Sends the specified {@link Message}
     *
     * <p>Example usage:
     * <blockquote><pre>
     * source.sendMessage(Message.of(MessageKey.MESSAGE_KEY, MessagePlaceholder.placeholder("%something%", foo)));
     * </pre></blockquote>
     *
     * @param message the message to send
     * @author MrIvanPlays
     * @since v1.0.0
     */
    default void sendMessage(@NotNull Message message) {
        List<String> toSend = message.handlePlaceholders(
                TreasuryPlugin.getInstance().configAdapter().getMessages().getMessage(message.getKey())
        );
        if (toSend.size() == 1) {
            sendMessage(toSend.get(0));
            return;
        }
        for (String msg : toSend) {
            sendMessage(msg);
        }
    }

    /**
     * Sends the specified {@link String} {@code message}
     *
     * <p><b>CONTRIBUTOR WARNING: THIS SHOULD ONLY BE USED FOR DEBUGGING AND NOTHING ELSE.</b>
     *
     * @param message message to send
     * @author MrIvanPlays
     * @since v1.0.0
     */
    void sendMessage(@NotNull String message);

    /**
     * Checks if this source has the specified permission node.
     *
     * @param node the node you want to check
     * @return true if has permission, false otherwise
     * @author MrIvanPlays
     * @since v1.0.0
     */
    boolean hasPermission(@NotNull String node);
}
