package me.lokka30.treasury.plugin.core.config;

import java.util.Objects;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import me.lokka30.treasury.plugin.core.config.settings.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A config adapter, providing {@link Messages} and {@link Settings}
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public abstract class ConfigAdapter {

    private static ConfigAdapter instance;

    @Nullable
    public static ConfigAdapter getInstance() {
        return instance;
    }

    public static void setInstance(@NotNull ConfigAdapter newInstance) {
        Objects.requireNonNull(newInstance, "newInstance");
        if (instance != null) {
            throw new IllegalArgumentException("Instance already set!");
        }
        instance = newInstance;
    }

    @NotNull
    public abstract Messages getMessages();

    @NotNull
    public abstract Settings getSettings();
}
