package me.lokka30.treasury.plugin.core.config.messaging;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public abstract class Messages {

    private Map<MessageKey, String> messages;

    public Messages(@NotNull Map<String, String> messages) {
        Objects.requireNonNull(messages, "messages");
        this.messages = new HashMap<>();
        EnumSet<MessageKey> allKeys = EnumSet.allOf(MessageKey.class);
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            MessageKey current = MessageKey.fromConfigKey(entry.getKey());
            if (current == null) {
                // todo: what shall we do here?
                continue;
            }
            allKeys.remove(current);
            this.messages.put(current, entry.getValue());
        }
        if (allKeys.size() > 0) {
            this.generateMissingEntries(allKeys);
            for (MessageKey left : allKeys) {
                this.messages.put(left, left.getDefaultMessage());
            }
        }
    }

    @NotNull
    public String getMessage(@NotNull MessageKey key) {
        Objects.requireNonNull(key, "key");
        return messages.get(key);
    }

    public abstract void generateMissingEntries(@NotNull Collection<MessageKey> keys);
}
