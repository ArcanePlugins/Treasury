/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.papi;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.bukkit.hooks.papi.economy.EconomyHook;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreasuryPapiExpansion extends PlaceholderExpansion implements Configurable, Cacheable {

    private final String author;
    private final Collection<TreasuryPapiHook> hooks = new HashSet<>();

    public TreasuryPapiExpansion(@NotNull TreasuryBukkit plugin) {
        this.author = String.join(", ", plugin.getDescription().getAuthors());
        this.hooks.add(new EconomyHook(this, plugin));
    }

    @Override
    public boolean register() {
        // Remove all failing hooks.
        hooks.removeIf(hook -> !hook.setup());

        // If any hooks are active, register.
        if (!hooks.isEmpty()) {
            return super.register();
        }

        // Otherwise, do not enable the expansion.
        return false;
    }

    @Override
    public Map<String, Object> getDefaults() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("baltop.enabled", false);
        defaults.put("baltop.cache_size", 100);
        defaults.put("baltop.check_delay", 30);
        defaults.put("formatting.thousands", "k");
        defaults.put("formatting.millions", "M");
        defaults.put("formatting.billions", "B");
        defaults.put("formatting.trillions", "T");
        defaults.put("formatting.quadrillions", "Q");
        return defaults;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "treasury";
    }

    @Override
    public @NotNull String getAuthor() {
        return author;
    }

    @Override
    public @NotNull String getVersion() {
        return TreasuryPlugin.getInstance().getVersion().toString();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onRequest(@Nullable OfflinePlayer player, @NotNull String param) {
        for (TreasuryPapiHook hook : hooks) {
            if (param.startsWith(hook.getPrefix())) {
                // Pass request to specified hook sans prefix.
                return hook.onRequest(player, param.replace(hook.getPrefix(), ""));
            }
        }

        return null;
    }

    @Override
    public void clear() {
        hooks.forEach(TreasuryPapiHook::clear);
    }

}
