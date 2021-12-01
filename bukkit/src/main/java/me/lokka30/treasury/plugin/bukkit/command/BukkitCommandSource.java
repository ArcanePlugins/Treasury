package me.lokka30.treasury.plugin.bukkit.command;

import me.lokka30.treasury.plugin.bukkit.BukkitTreasuryPlugin;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BukkitCommandSource implements CommandSource {

    private final CommandSender sender;

    public BukkitCommandSource(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        sender.sendMessage(((BukkitTreasuryPlugin) TreasuryPlugin.getInstance()).colorize(message));
    }

    @Override
    public boolean hasPermission(@NotNull String node) {
        return sender.hasPermission(node);
    }
}
