/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {

    private final String author;
    private TreasuryPAPIHook economy;

    public PAPIExpansion(TreasuryBukkit plugin) {
        this.author = String.join(", ", plugin.getDescription().getAuthors());
        this.economy = new EconomyHook();
    }

    @Override
    public boolean register() {
        if (!economy.setup()) {
            economy = null;
        }
        if (economy != null) {
            return super.register();
        }
        return false;
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
        return economy != null && economy.canRegister();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String param) {
        if (economy != null && param.startsWith("economy_")) {
            return economy.onRequest(player, param.replace("economy_", ""));
        }

        return null;
    }

}
