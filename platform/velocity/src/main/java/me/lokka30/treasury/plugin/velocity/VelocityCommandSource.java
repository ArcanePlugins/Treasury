/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.velocity;

import com.velocitypowered.api.proxy.Player;
import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class VelocityCommandSource implements CommandSource {

    private final com.velocitypowered.api.command.CommandSource handle;
    private final Cause<?> cause;

    public VelocityCommandSource(com.velocitypowered.api.command.CommandSource handle) {
        this.handle = handle;
        if (handle instanceof Player) {
            this.cause = Cause.player(((Player) handle).getUniqueId());
        } else {
            this.cause = Cause.SERVER;
        }
    }

    @Override
    public void sendMessage(@NotNull final String message) {
        handle.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }

    @Override
    public boolean hasPermission(@NotNull final String node) {
        return handle.hasPermission(node);
    }

    @Override
    public @NotNull Cause<?> getAsCause() {
        return cause;
    }

}
