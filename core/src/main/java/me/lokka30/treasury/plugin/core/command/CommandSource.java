package me.lokka30.treasury.plugin.core.command;

import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
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
        sendMessage(message.handlePlaceholders(ConfigAdapter.getInstance().getMessages().getMessage(message.getKey())));
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
