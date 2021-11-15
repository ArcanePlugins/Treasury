package me.lokka30.treasury.plugin.core.config.settings;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.debug.DebugCategory;
import me.lokka30.treasury.plugin.core.debug.DebugCategoryMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SettingKey<T> {

    public static SettingKey<Boolean> CHECK_UPDATES = of(
            "update-checker.enabled", true, Boolean.class
    );

    public static SettingKey<DebugCategoryMode> DEBUG_CATEGORY_MODE = of(
            "debug.enabled-categories.mode", DebugCategoryMode.NOT_SPECIFIED, DebugCategoryMode.class
    );

    public static SettingKey<EnumSet<DebugCategory>> DEBUG_CATEGORY_LIST = of(
            EnumSet.noneOf(DebugCategory.class),
            new Function<Settings, EnumSet<DebugCategory>>() {

                private EnumSet<DebugCategory> cached = null;

                @Override
                public EnumSet<DebugCategory> apply(Settings settings) {
                    if (cached != null) {
                        return cached;
                    }
                    cached = EnumSet.noneOf(DebugCategory.class);
                    EnumSet<DebugCategory> listed = EnumSet.noneOf(DebugCategory.class);
                    List<String> configList = settings.getStringList("debug.enabled-categories.list");
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
                    return cached;
                }
            }
    );

    @NotNull
    public static <T> SettingKey<T> of(@NotNull T def, @NotNull Function<Settings, T> specialMapper) {
        return new SettingKey<>(def, specialMapper);
    }

    @NotNull
    public static <T> SettingKey<T> of(@NotNull String key, @NotNull T def, @NotNull Class<T> type) {
        return new SettingKey<>(key, def, type);
    }

    private final String key;
    private final T def;
    private final Class<T> type;
    private final Function<Settings, T> specialMapper;

    private SettingKey(@NotNull String key, @NotNull T def, @NotNull Class<T> type) {
        this.key = Objects.requireNonNull(key, "key");
        this.def = Objects.requireNonNull(def, "def");
        this.type = Objects.requireNonNull(type, "type");
        this.specialMapper = null;
    }

    private SettingKey(@NotNull T def, @NotNull Function<Settings, T> specialMapper) {
        this.key = null;
        this.def = Objects.requireNonNull(def, "def");
        this.type = null;
        this.specialMapper = Objects.requireNonNull(specialMapper, "specialMapper");
    }

    @Nullable
    public String getKey() {
        return key;
    }

    @NotNull
    public T getDefault() {
        return def;
    }

    @Nullable
    public Class<T> getType() {
        return type;
    }

    @Nullable
    public Function<Settings, T> getSpecialMapper() {
        return specialMapper;
    }

}
