/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.minestom;

import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MinestomCommandSource implements CommandSource {

    private final CommandSender handle;
    private final Cause<?> cause;

    public MinestomCommandSource(CommandSender handle) {
        this.handle = handle;
        if (handle instanceof Player) {
            this.cause = Cause.player(((Player) handle).getUuid());
        } else {
            this.cause = Cause.SERVER;
        }
    }

    @Override
    public void sendMessage(@NotNull final String message) {
        handle.sendMessage(((MinestomTreasuryPlugin) TreasuryPlugin.getInstance()).deserialize(message));
    }

    @Override
    public boolean hasPermission(@NotNull final String node) {
        return !(handle instanceof Player) || handle.hasPermission(node);
    }

    @Override
    public @NotNull Cause<?> getAsCause() {
        return this.cause;
    }

}
