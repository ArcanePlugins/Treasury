/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import java.util.HashMap;
import java.util.Map;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion implements Configurable {

    private final String author;
    private TreasuryPAPIHook economy;

    public PAPIExpansion(TreasuryBukkit plugin) {
        this.author = String.join(", ", plugin.getDescription().getAuthors());
        this.economy = new EconomyHook(this, plugin);
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
        return economy != null && economy.canRegister();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String param) {
        if (economy != null && param.startsWith("eco_")) {
            return economy.onRequest(player, param.replace("eco_", ""));
        }

        return null;
    }

}
