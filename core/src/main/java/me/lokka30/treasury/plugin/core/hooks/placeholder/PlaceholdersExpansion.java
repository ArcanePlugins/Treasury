/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.hooks.placeholder;

import java.util.HashSet;
import java.util.Set;
import me.lokka30.treasury.plugin.core.hooks.PlayerData;
import me.lokka30.treasury.plugin.core.hooks.placeholder.economy.EconomyHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlaceholdersExpansion {

    private Set<SpecificPlaceholderHook> hooks = new HashSet<>();

    public PlaceholdersExpansion() {
        hooks.add(new EconomyHook(this));
    }

    public boolean register() {
        // Remove all failing hooks.
        hooks.removeIf(hook -> !hook.setup());

        // If any hooks are active, register.
        // Otherwise, do not enable the expansion.
        return !hooks.isEmpty();
    }

    public void clear() {
        hooks.forEach(SpecificPlaceholderHook::clear);
    }

    @Nullable
    public String onRequest(@Nullable PlayerData playerData, @NotNull String param) {
        for (SpecificPlaceholderHook hook : hooks) {
            if (param.startsWith(hook.prefix())) {
                // Pass request
                return hook.onRequest(playerData, param.replace(hook.prefix(), ""));
            }
        }

        return null;
    }

    public abstract String pluginName();

    /**
     * Get a string from the specific config where stuff about placeholder expansions is held.
     *
     * @param key the config key
     * @param def the default value
     * @return value
     */
    public abstract String getString(@NotNull String key, String def);

    // the getString javadoc is also applied for the next 2 methods
    // except it's not a string

    public abstract int getInt(@NotNull String key, int def);

    public abstract boolean getBoolean(@NotNull String key, boolean def);
}
