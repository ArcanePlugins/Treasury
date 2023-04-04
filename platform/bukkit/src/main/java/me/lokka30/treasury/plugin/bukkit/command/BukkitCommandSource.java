/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.command;

import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.plugin.bukkit.BukkitTreasuryPlugin;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitCommandSource implements CommandSource {

    private final CommandSender sender;
    private final Cause<?> cause;

    public BukkitCommandSource(CommandSender sender) {
        this.sender = sender;
        if (sender instanceof Player) {
            this.cause = Cause.player(((Player) sender).getUniqueId());
        } else {
            this.cause = Cause.SERVER;
        }
    }

    @Override
    public void sendMessage(@NotNull String message) {
        sender.sendMessage(((BukkitTreasuryPlugin) TreasuryPlugin.getInstance()).colorize(message));
    }

    @Override
    public boolean hasPermission(@NotNull String node) {
        return sender.hasPermission(node);
    }

    @Override
    public @NotNull Cause<?> getAsCause() {
        return this.cause;
    }

}
