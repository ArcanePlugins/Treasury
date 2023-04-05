/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge;

import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.permission.PermissionChecker;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeCommandSource implements CommandSource {

    private final Audience handle;

    private final Cause<?> cause;

    public SpongeCommandSource(Audience handle) {
        this.handle = handle;
        if (handle instanceof Player) {
            this.cause = Cause.player(((Player) handle).uniqueId());
        } else {
            this.cause = Cause.SERVER;
        }
    }

    @Override
    public void sendMessage(@NotNull final String message) {
        handle.sendMessage(((SpongeTreasuryPlugin) TreasuryPlugin.getInstance()).color(message));
    }

    @Override
    public boolean hasPermission(@NotNull final String node) {
        if (this.cause.equals(Cause.SERVER)) {
            return true;
        } else {
            return this.handle.get(PermissionChecker.POINTER).get().test(node);
        }
    }

    @Override
    public @NotNull Cause<?> getAsCause() {
        return this.cause;
    }

}
