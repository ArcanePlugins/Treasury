package me.lokka30.treasury.plugin.core.config.settings;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
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
    default <T> T getSetting(@NotNull SettingKey<T> settingKey) {
        Objects.requireNonNull(settingKey, "settingKey");
        // todo: temporary solution. shall have this as an implementation
        //       currently to show how it should be handled first time.
        if (settingKey.getSpecialMapper() != null) {
            return settingKey.getSpecialMapper().apply(this);
        }
        Object value = getSetting(settingKey.getKey());
        if (value == null) {
            generateMissingOption(settingKey);
            return settingKey.getDefault();
        }
        Class<T> type = settingKey.getType();
        if (type.isAssignableFrom(value.getClass())) {
            return settingKey.getType().cast(value);
        }
        if (type.isEnum()) {
            String valString = String.valueOf(value).toUpperCase(Locale.ROOT);
            for (T eConst : type.getEnumConstants()) {
                String name = eConst.toString();
                if (valString.equalsIgnoreCase(name)) {
                    return eConst;
                }
            }
            TreasuryPlugin.getInstance().logger().error(
                    "Invalid enum constant " + value + " for config option " + settingKey.getKey()
            );
        }
        return settingKey.getDefault();
    }

    /**
     * Treasury core calls this method whenever it stumbles upon a non generated setting key, in order to generate it.
     *
     * @param key key to generate
     * @param <T> value type
     */
    <T> void generateMissingOption(@NotNull SettingKey<T> key);

    /**
     * Returns the raw setting bound to the key specified.
     *
     * @param key
     * @return
     */
    @Nullable
    Object getSetting(@NotNull String key);

    @Nullable
    List<String> getStringList(@NotNull String key);
}
