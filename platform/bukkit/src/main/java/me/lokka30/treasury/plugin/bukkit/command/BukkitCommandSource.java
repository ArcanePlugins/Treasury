/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.command;

import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.plugin.bukkit.BukkitTreasuryPlugin;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitCommandSource implements CommandSource {

    private final CommandSender sender;
    private final EconomyTransactionInitiator<?> initiator;

    public BukkitCommandSource(CommandSender sender) {
        this.sender = sender;
        if (sender instanceof Player) {
            this.initiator = EconomyTransactionInitiator.createInitiator(EconomyTransactionInitiator.Type.PLAYER, ((Player) sender).getUniqueId());
        } else {
            this.initiator = EconomyTransactionInitiator.SERVER;
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
    public @NotNull EconomyTransactionInitiator<?> getAsTransactionInitiator() {
        return initiator;
    }

}
