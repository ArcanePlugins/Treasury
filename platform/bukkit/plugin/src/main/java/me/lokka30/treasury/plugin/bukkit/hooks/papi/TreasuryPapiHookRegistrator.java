/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.papi;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.bukkit.hooks.Hook;
import org.jetbrains.annotations.NotNull;

public class TreasuryPapiHookRegistrator implements Hook {

    @Override
    public @NotNull String getPlugin() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean register(@NotNull TreasuryBukkit plugin) {
        return new TreasuryPapiExpansion(plugin).register();
    }

}
