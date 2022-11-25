/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge;

import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.permission.PermissionChecker;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeCommandSource implements CommandSource {

    private final Audience handle;

    private final EconomyTransactionInitiator<?> initiator;

    public SpongeCommandSource(Audience handle) {
        this.handle = handle;
        if (handle instanceof Player) {
            this.initiator = EconomyTransactionInitiator.createInitiator(
                    EconomyTransactionInitiator.Type.PLAYER,
                    ((Player) handle).uniqueId()
            );
        } else {
            this.initiator = EconomyTransactionInitiator.SERVER;
        }
    }

    @Override
    public void sendMessage(@NotNull final String message) {
        handle.sendMessage(((SpongeTreasuryPlugin) TreasuryPlugin.getInstance()).color(message));
    }

    @Override
    public boolean hasPermission(@NotNull final String node) {
        if (this.initiator.getType() == EconomyTransactionInitiator.Type.SERVER) {
            return true;
        } else {
            return this.handle.get(PermissionChecker.POINTER).get().test(node);
        }
    }

    @Override
    public @NotNull EconomyTransactionInitiator<?> getAsTransactionInitiator() {
        return this.initiator;
    }

}
