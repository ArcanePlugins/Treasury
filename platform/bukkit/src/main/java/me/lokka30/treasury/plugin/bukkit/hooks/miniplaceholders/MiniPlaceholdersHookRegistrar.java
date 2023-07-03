/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.miniplaceholders;

import io.github.miniplaceholders.api.Expansion;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.bukkit.hooks.Hook;
import me.lokka30.treasury.plugin.bukkit.vendor.BukkitVendor;
import org.jetbrains.annotations.NotNull;

public class MiniPlaceholdersHookRegistrar implements Hook {

    private Expansion expansion;

    @Override
    public @NotNull String getPlugin() {
        return "MiniPlaceholders";
    }

    @Override
    public boolean register(@NotNull final TreasuryBukkit plugin) {
        if (!BukkitVendor.isPaper()) { // Paper, Folia
            return false;
        }
        // TODO: Seek help from 4drian3d !!!
        this.expansion = Expansion.builder("treasury")
                .build();
        expansion.register();
        return expansion.registered();
    }

    @Override
    public void shutdown() {
        expansion.unregister();
    }

}
