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

public abstract class Messages {

    public static final class MessageHolder {

        private List<String> messages;

        public MessageHolder(String message) {
            this.messages = Collections.singletonList(message);
        }

        public MessageHolder(List<String> messages) {
            this.messages = messages;
        }

        public List<String> getMessages() {
            return messages;
        }

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

    @NotNull
    public List<String> getMessage(@NotNull MessageKey key) {
        Objects.requireNonNull(key, "key");
        return messages.get(key).getMessages();
    }

    @NotNull
    public String getSingleMessage(@NotNull MessageKey key) {
        Objects.requireNonNull(key, "key");
        return messages.get(key).single();
    }

    public abstract void generateMissingEntries(@NotNull Collection<MessageKey> keys);
}
