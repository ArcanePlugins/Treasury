/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command;

import java.util.List;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import org.jetbrains.annotations.NotNull;

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
