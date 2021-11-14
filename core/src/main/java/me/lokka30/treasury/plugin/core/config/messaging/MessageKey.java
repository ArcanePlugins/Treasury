package me.lokka30.treasury.plugin.core.config.messaging;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import me.lokka30.treasury.plugin.core.config.ConfigVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// todo: fill this with messages
public enum MessageKey {
    ;

    private static final Map<String, MessageKey> BY_CONFIG_KEY = new HashMap<>();
    private static final Multimap<ConfigVersion, MessageKey> BY_CONFIG_VERSION = HashMultimap.create();

    static {
        for (MessageKey messageKey : MessageKey.values()) {
            BY_CONFIG_KEY.put(messageKey.asConfigKey(), messageKey);
            BY_CONFIG_VERSION.put(messageKey.getVersionAddedIn(), messageKey);
        }
    }

    @Nullable
    public static Collection<MessageKey> getAllWithConfigVersion(@NotNull ConfigVersion configVersion) {
        Objects.requireNonNull(configVersion, "configVersion");
        return BY_CONFIG_VERSION.get(configVersion);
    }

    @Nullable
    public static MessageKey fromConfigKey(@NotNull String configKey) {
        Objects.requireNonNull(configKey, "configKey");
        return BY_CONFIG_KEY.get(configKey);
    }

    private final String configKey, defaultMessage;
    private final ConfigVersion versionAddedIn;

    MessageKey(@NotNull String configKey, @NotNull String defaultMessage, @NotNull ConfigVersion versionAddedIn) {
        this.configKey = Objects.requireNonNull(configKey, "configKey");
        this.defaultMessage = Objects.requireNonNull(defaultMessage, "defaultMessage");
        this.versionAddedIn = Objects.requireNonNull(versionAddedIn, "versionAddedIn");
    }

    @NotNull
    public String asConfigKey() {
        return configKey;
    }

    @NotNull
    public String getDefaultMessage() {
        return defaultMessage;
    }

    @NotNull
    public ConfigVersion getVersionAddedIn() {
        return versionAddedIn;
    }

}
