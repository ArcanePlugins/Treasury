package me.lokka30.treasury.plugin.core.config.settings;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a settings object.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public interface Settings {

    /**
     * Returns the specified setting value, which may be null.
     *
     * @param settingKey setting key
     * @param <T> value type
     * @return value, or null
     */
    @NotNull
    <T> T getSetting(@NotNull SettingKey<T> settingKey);

    /**
     * Returns the raw setting bound to the key specified.
     *
     * @param key the key you want the value of
     * @return value or null
     */
    @Nullable
    Object getSetting(@NotNull String key);

    /**
     * Returns the string list setting bound to the key specified.
     *
     * @param key the key you want the string list for
     * @return value or null
     */
    @Nullable
    List<String> getStringList(@NotNull String key);
}
