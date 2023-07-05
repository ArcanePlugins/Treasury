/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.miniplaceholders;

import io.github.miniplaceholders.api.Expansion;
import java.io.File;
import java.util.Locale;
import java.util.UUID;
import me.lokka30.treasury.plugin.bukkit.BukkitTreasuryPlugin;
import me.lokka30.treasury.plugin.bukkit.vendor.BukkitVendor;
import me.lokka30.treasury.plugin.core.hooks.Hook;
import me.lokka30.treasury.plugin.core.hooks.PlayerData;
import me.lokka30.treasury.plugin.core.hooks.placeholder.BasicPlaceholderExpansion;
import me.lokka30.treasury.plugin.core.hooks.placeholder.PlaceholdersConfig;
import me.lokka30.treasury.plugin.core.hooks.placeholder.minipspecific.MiniPlaceholdersConfig;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MiniPlaceholdersHook implements Hook {

    private BasicPlaceholderExpansion basicExpansion;

    @Override
    public @NotNull String getPlugin() {
        return "MiniPlaceholders";
    }

    @Override
    public boolean register() {
        PlaceholdersConfig config = MiniPlaceholdersConfig.load(new File(
                ((BukkitTreasuryPlugin) BukkitTreasuryPlugin.getInstance()).dataFolder(),
                "miniPlaceholders_config.yml"
        ));
        this.basicExpansion = new BasicPlaceholderExpansion(config);
        Expansion expansion = Expansion.builder("treasury").filter(Player.class).globalPlaceholder(
                "eco_na",
                (queue, ctx) -> {
                    String argument = queue.popOr("No placeholder provided").value();
                    String parsed = basicExpansion.onRequest(null, "eco_" + argument);
                    if (parsed == null) {
                        return Tag.preProcessParsed("");
                    }
                    return Tag.preProcessParsed(parsed);
                }
        ).audiencePlaceholder("eco", (aud, queue, ctx) -> {
            String argument = queue.popOr("No placeholder provided").value();
            String parsed = basicExpansion.onRequest(new PlayerData() {
                @Override
                public @Nullable String name() {
                    return aud.get(Identity.NAME).orElse(null);
                }

                @Override
                public @Nullable UUID uniqueId() {
                    return aud.get(Identity.UUID).orElse(null);
                }

                @Override
                public @Nullable String getLocale() {
                    Locale locale = aud.get(Identity.LOCALE).orElse(null);
                    return locale == null ? null : locale.toString();
                }
            }, "eco_" + argument);
            if (parsed == null) {
                return Tag.preProcessParsed("");
            }
            return Tag.preProcessParsed(parsed);
        }).build();
        expansion.register();
        return basicExpansion.register() && expansion.registered();
    }

    @Override
    public void shutdown() {
        if (basicExpansion != null) {
            basicExpansion.clear();
        }
    }

}
