/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bungeecord;

import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

public class BungeeCommandSource implements CommandSource {

    private final CommandSender sender;
    private final EconomyTransactionInitiator<?> initiator;

    public BungeeCommandSource(CommandSender sender) {
        this.sender = sender;
        if (sender instanceof ProxiedPlayer) {
            this.initiator = EconomyTransactionInitiator.createInitiator(
                    EconomyTransactionInitiator.Type.PLAYER,
                    ((ProxiedPlayer) sender).getUniqueId()
            );
        } else {
            this.initiator = EconomyTransactionInitiator.SERVER;
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
    public @NotNull EconomyTransactionInitiator<?> getAsTransactionInitiator() {
        return initiator;
    }

}
