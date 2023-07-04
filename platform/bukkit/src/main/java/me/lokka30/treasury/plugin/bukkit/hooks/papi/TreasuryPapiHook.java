/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.papi;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.hooks.Hook;
import org.jetbrains.annotations.NotNull;

public class TreasuryPapiHook implements Hook {

    private TreasuryPapiExpansion expansion;
    private final TreasuryBukkit plugin;

    public TreasuryPapiHook(final TreasuryBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getPlugin() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean register() {
        if (expansion == null) {
            expansion = new TreasuryPapiExpansion(plugin);
        }
        return expansion.register();
    }

    @Override
    public void shutdown() {
        if (expansion != null) {
            expansion.clear();
        }
    }

}
