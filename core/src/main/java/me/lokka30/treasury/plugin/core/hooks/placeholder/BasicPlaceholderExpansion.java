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

public final class BasicPlaceholderExpansion {

    private Set<SpecificPlaceholderHook> hooks = new HashSet<>();

    private final PlaceholdersConfig placeholdersConfig;

    public BasicPlaceholderExpansion(PlaceholdersConfig placeholdersConfig) {
        this.placeholdersConfig = placeholdersConfig;
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

    public PlaceholdersConfig getPlaceholdersConfig() {
        return this.placeholdersConfig;
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

}
