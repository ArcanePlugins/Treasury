/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.krypton;

import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.api.command.ConsoleSender;
import org.kryptonmc.api.command.Sender;

public class KryptonCommandSource implements CommandSource {

    private final Sender handle;
    private final Cause<?> cause;

    public KryptonCommandSource(Sender handle) {
        this.handle = handle;
        this.cause = handle instanceof ConsoleSender ? Cause.SERVER : Cause.player(handle
                .get(Identity.UUID)
                .get());
    }

    @Override
    public void sendMessage(@NotNull String message) {
        handle.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }

    @Override
    public boolean hasPermission(@NotNull String node) {
        return handle instanceof ConsoleSender || handle.hasPermission(node);
    }

    @Override
    public @NotNull Cause<?> getAsCause() {
        return this.cause;
    }

}
