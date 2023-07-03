/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.miniplaceholders;

import io.github.miniplaceholders.api.Expansion;
import java.io.File;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.bukkit.hooks.Hook;
import me.lokka30.treasury.plugin.bukkit.vendor.BukkitVendor;
import me.lokka30.treasury.plugin.core.hooks.placeholder.BasicPlaceholderExpansion;
import me.lokka30.treasury.plugin.core.hooks.placeholder.PlaceholdersConfig;
import me.lokka30.treasury.plugin.core.hooks.placeholder.minipspecific.MiniPlaceholdersConfig;
import org.jetbrains.annotations.NotNull;

public class MiniPlaceholdersHookRegistrar implements Hook {

    private BasicPlaceholderExpansion basicExpansion;

    @Override
    public @NotNull String getPlugin() {
        return "MiniPlaceholders";
    }

    @Override
    public boolean register(@NotNull final TreasuryBukkit plugin) {
        if (!BukkitVendor.isPaper() || !BukkitVendor.isFolia()) {
            return false;
        }
        PlaceholdersConfig config = MiniPlaceholdersConfig.load(new File(
                plugin.getDataFolder(),
                "miniPlaceholders_config.yml"
        ));
        this. basicExpansion = new BasicPlaceholderExpansion(config);
        // TODO: Seek help from 4drian3d !!!
        Expansion expansion = Expansion.builder("treasury").build();
        expansion.register();
        return expansion.registered();
    }

    @Override
    public void shutdown() {
        if (basicExpansion != null) {
            basicExpansion.clear();
        }
    }

}
