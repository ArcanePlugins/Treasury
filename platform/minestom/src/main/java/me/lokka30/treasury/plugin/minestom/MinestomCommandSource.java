/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.minestom;

import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MinestomCommandSource implements CommandSource {

    private final CommandSender handle;
    private final EconomyTransactionInitiator<?> initiator;

    public MinestomCommandSource(CommandSender handle) {
        this.handle = handle;
        if (handle instanceof Player) {
            this.initiator = EconomyTransactionInitiator.createInitiator(
                    EconomyTransactionInitiator.Type.PLAYER,
                    ((Player) handle).getUuid()
            );
        } else {
            this.initiator = EconomyTransactionInitiator.SERVER;
        }
    }

    @Override
    public void sendMessage(@NotNull final String message) {
        handle.sendMessage(((MinestomTreasuryPlugin) TreasuryPlugin.getInstance()).deserialize(message));
    }

    @Override
    public boolean hasPermission(@NotNull final String node) {
        return handle.hasPermission(node);
    }

    @Override
    public @NotNull EconomyTransactionInitiator<?> getAsTransactionInitiator() {
        return this.initiator;
    }

}
