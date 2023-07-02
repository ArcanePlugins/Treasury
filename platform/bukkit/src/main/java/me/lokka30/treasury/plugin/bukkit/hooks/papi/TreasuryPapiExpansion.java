/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.papi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.hooks.PlayerData;
import me.lokka30.treasury.plugin.core.hooks.placeholder.PlaceholdersExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreasuryPapiExpansion extends PlaceholderExpansion implements Configurable, Cacheable {

    private final String author;
    private final PlaceholdersExpansion base;

    public TreasuryPapiExpansion(@NotNull TreasuryBukkit plugin) {
        this.author = String.join(", ", plugin.getDescription().getAuthors());
        this.base = new PlaceholdersExpansionImplPapi(this::getString,
                this::getInt,
                this::getBoolean
        );
    }

    @Override
    public boolean register() {
        return base.register() && super.register();
    }

    @Override
    public Map<String, Object> getDefaults() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("baltop.enabled", false);
        defaults.put("baltop.cache_size", 100);
        defaults.put("baltop.check_delay", 30);
        defaults.put("balance.cache_check_delay", 60);
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
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(@Nullable OfflinePlayer player, @NotNull String param) {
        return base.onRequest(new PlayerData() {
            @Override
            public @Nullable String name() {
                return player != null ? player.getName() : null;
            }

            @Override
            public @Nullable UUID uniqueId() {
                return player != null ? player.getUniqueId() : null;
            }

            @Override
            public @Nullable String getLocale() {
                if (player == null) {
                    return null;
                }

                Player onlinePlayer = player.getPlayer();
                return onlinePlayer != null ? onlinePlayer.getLocale() : null;
            }
        }, param);
    }

    @Override
    public void clear() {
        base.clear();
    }

}
