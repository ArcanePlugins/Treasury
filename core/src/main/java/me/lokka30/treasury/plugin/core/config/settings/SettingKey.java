package me.lokka30.treasury.plugin.core.config.settings;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.ConfigVersion;
import me.lokka30.treasury.plugin.core.debug.DebugCategory;
import me.lokka30.treasury.plugin.core.debug.DebugCategoryMode;
import me.lokka30.treasury.plugin.core.utils.ExpiringCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a key of a setting, containing information
 * about the default value of it, config version, and value type.
 *
 * @param <T> value type
 * @author MrIvanPlays
 * @since v1.0.0
 */
public final class SettingKey<T> {

    public static SettingKey<Boolean> CHECK_UPDATES = of(
            ConfigVersion.V1, "update-checker.enabled", true, Boolean.class
    );

    public static SettingKey<DebugCategoryMode> DEBUG_CATEGORY_MODE = of(
            ConfigVersion.V1, "debug.enabled-categories.mode", DebugCategoryMode.WHITELIST, DebugCategoryMode.class
    );

    public static SettingKey<EnumSet<DebugCategory>> DEBUG_CATEGORY_LIST = of(
            ConfigVersion.V1,
            EnumSet.noneOf(DebugCategory.class),
            new Function<Settings, EnumSet<DebugCategory>>() {

                private ExpiringCache<EnumSet<DebugCategory>> cache = new ExpiringCache<>(10, TimeUnit.MINUTES);

                @Override
                public EnumSet<DebugCategory> apply(Settings settings) {
                    EnumSet<DebugCategory> cached = cache.get();
                    if (cached != null) {
                        return cached;
                    }
                    cached = EnumSet.noneOf(DebugCategory.class);
                    EnumSet<DebugCategory> listed = EnumSet.noneOf(DebugCategory.class);
                    List<String> configList = settings.getStringList("debug.enabled-categories.list");
                    if (configList == null || configList.isEmpty()) {
                        return cached;
                    }
                    for (String categoryStr : configList) {
                        try {
                            listed.add(DebugCategory.valueOf(categoryStr));
                        } catch (IllegalArgumentException e) {
                            TreasuryPlugin.getInstance().logger().error(
                                    "Invalid DebugCategory '&b"
                                            + categoryStr
                                            + "&7' specified in &bsettings.yml&7 at location "
                                            + "'&bdebug.enabled-categories.list&7'! Please fix this ASAP."
                            );
                        }
                    }
                    switch (settings.getSetting(SettingKey.DEBUG_CATEGORY_MODE)) {
                        case WHITELIST:
                            cached.addAll(listed);
                            break;
                        case BLACKLIST:
                            cached.addAll(EnumSet.allOf(DebugCategory.class));
                            cached.removeAll(listed);
                            break;
                        default:
                            TreasuryPlugin.getInstance().logger().error(
                                    "Invalid mode specified in &bsettings.yml&7 at location "
                                            + "'&bdebug.enabled-categories.mode&7'! "
                                            + "You can only use '&bWHITELIST&7' or '&bBLACKLIST&7'. Please fix this ASAP."
                            );
                    }
                    cache.set(cached);
                    return cached;
                }
            }
    );

    /**
     * Creates a new {@link SettingKey}
     *
     * @param configVersion config version in which this option was added
     * @param def default value
     * @param specialMapper to value mapper
     * @param <T> value type
     * @return setting key instance
     */
    @NotNull
    public static <T> SettingKey<T> of(
            @NotNull ConfigVersion configVersion,
            @NotNull T def,
            @NotNull Function<Settings, T> specialMapper
    ) {
        return new SettingKey<>(configVersion, def, specialMapper);
    }

    /**
     * Creates a new {@link SettingKey}
     *
     * @param configVersion config version in which this option was added
     * @param key key in config
     * @param def default value
     * @param type value type class
     * @param <T> value type
     * @return setting key instance
     */
    @NotNull
    public static <T> SettingKey<T> of(
            @NotNull ConfigVersion configVersion,
            @NotNull String key,
            @NotNull T def,
            @NotNull Class<T> type
    ) {
        return new SettingKey<>(configVersion, key, def, type);
    }

    private final ConfigVersion configVersion;
    private final String key;
    private final T def;
    private final Class<T> type;
    private final Function<Settings, T> specialMapper;

    private SettingKey(
            @NotNull ConfigVersion configVersion,
            @NotNull String key,
            @NotNull T def,
            @NotNull Class<T> type) {
        this.configVersion = Objects.requireNonNull(configVersion, "configVersion");
        this.key = Objects.requireNonNull(key, "key");
        this.def = Objects.requireNonNull(def, "def");
        this.type = Objects.requireNonNull(type, "type");
        this.specialMapper = null;
    }

    private SettingKey(@NotNull ConfigVersion configVersion, @NotNull T def, @NotNull Function<Settings, T> specialMapper) {
        this.configVersion = Objects.requireNonNull(configVersion, "configVersion");
        this.key = null;
        this.def = Objects.requireNonNull(def, "def");
        this.type = null;
        this.specialMapper = Objects.requireNonNull(specialMapper, "specialMapper");
    }

    /**
     * Returns the config version this setting was added.
     *
     * @return config version
     */
    @NotNull
    public ConfigVersion getConfigVersion() {
        return configVersion;
    }

    /**
     * Returns the key of this setting.
     *
     * @return key
     */
    @Nullable
    public String getKey() {
        return key;
    }

    /**
     * Returns the default value for this setting.
     *
     * @return default value
     */
    @NotNull
    public T getDefault() {
        return def;
    }

    /**
     * Returns the type class, held by this setting.
     *
     * @return type class
     */
    @Nullable
    public Class<T> getType() {
        return type;
    }

    /**
     * Returns a value mapper which is responsible to get the value raw and parse it.
     *
     * @return value mapper
     */
    @Nullable
    public Function<Settings, T> getSpecialMapper() {
        return specialMapper;
    }

}
