package me.lokka30.treasury.plugin.core.config.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a messages handler.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public abstract class Messages {

    /**
     * Represents a holder of a message.
     *
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public static final class MessageHolder {

        private List<String> messages;

        public MessageHolder(@NotNull String message) {
            this.messages = Collections.singletonList(Objects.requireNonNull(message, "message"));
        }

        public MessageHolder(@NotNull List<String> messages) {
            this.messages = Objects.requireNonNull(messages, "messages");
        }

        /**
         * Returns the messages held by this message holder.
         *
         * @return messages
         */
        @NotNull
        public List<String> getMessages() {
            return messages;
        }

        /**
         * Returns the single message held by this message holder.
         *
         * @return single message
         */
        @NotNull
        public String single() {
            return messages.get(0);
        }

    }

    private Map<MessageKey, MessageHolder> messages;

    public Messages(@NotNull Map<String, Object> messages) {
        Objects.requireNonNull(messages, "messages");
        this.messages = new HashMap<>();
        EnumSet<MessageKey> allKeys = EnumSet.allOf(MessageKey.class);
        for (Map.Entry<String, Object> entry : messages.entrySet()) {
            MessageKey current = MessageKey.fromConfigKey(entry.getKey());
            if (current == null) {
                TreasuryPlugin.getInstance().logger().warn("Invalid message key '" + entry.getKey() + "' found");
                continue;
            }
            allKeys.remove(current);
            if (entry.getValue() instanceof String) {
                this.messages.put(current, new MessageHolder(String.valueOf(entry.getValue())));
            } else if (entry.getValue() instanceof Iterable) {
                List<String> values = new ArrayList<>();
                for (Object val : (Iterable) entry.getValue()) {
                    values.add(String.valueOf(val));
                }
                this.messages.put(current, new MessageHolder(values));
            }
            TreasuryPlugin.getInstance().logger().warn("Invalid message at " + current.asConfigKey() + " , using default value");
            this.messages.put(current, current.getDefaultMessage());
        }
        if (allKeys.size() > 0) {
            this.generateMissingEntries(allKeys);
            for (MessageKey left : allKeys) {
                this.messages.put(left, left.getDefaultMessage());
            }
        }
    }

    /**
     * Returns the messages held by the specified {@link MessageKey} {@code key}
     *
     * @param key the key you want the messages for
     * @return messages of the specified key
     */
    @NotNull
    public List<String> getMessage(@NotNull MessageKey key) {
        Objects.requireNonNull(key, "key");
        return messages.get(key).getMessages();
    }

    /**
     * Returns the single message held by the specified {@link MessageKey} {@code key}
     *
     * @param key the key you want the message of
     * @return message of the specified key
     */
    @NotNull
    public String getSingleMessage(@NotNull MessageKey key) {
        Objects.requireNonNull(key, "key");
        return messages.get(key).single();
    }

    /**
     * Should generate the specified missing {@link MessageKey MessageKeys} {@code keys} in the messages file.
     *
     * @param keys the keys to generate
     */
    public abstract void generateMissingEntries(@NotNull Collection<MessageKey> keys);
}
