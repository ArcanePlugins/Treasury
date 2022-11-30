/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.papi;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.bukkit.hooks.Hook;
import org.jetbrains.annotations.NotNull;

public class TreasuryPapiHookRegistrar implements Hook {

    private TreasuryPapiExpansion expansion;

    @Override
    public @NotNull String getPlugin() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean register(@NotNull TreasuryBukkit plugin) {
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
