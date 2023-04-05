/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bungeecord;

import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

public class BungeeCommandSource implements CommandSource {

    private final CommandSender sender;
    private final Cause<?> cause;

    public BungeeCommandSource(CommandSender sender) {
        this.sender = sender;
        if (sender instanceof ProxiedPlayer) {
            this.cause = Cause.player(((ProxiedPlayer) sender).getUniqueId());
        } else {
            this.cause = Cause.SERVER;
        }
    }

    @Override
    public void sendMessage(@NotNull final String message) {
        sender.sendMessage(TextComponent.fromLegacyText(((BungeeTreasuryPlugin) TreasuryPlugin.getInstance()).colorize(
                message)));
    }

    @Override
    public boolean hasPermission(@NotNull final String node) {
        return sender.hasPermission(node);
    }

    @Override
    public @NotNull Cause<?> getAsCause() {
        return this.cause;
    }

}
