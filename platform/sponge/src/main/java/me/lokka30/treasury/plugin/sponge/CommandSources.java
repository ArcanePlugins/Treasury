/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

public class CommandSources {

    private static CommandSource CONSOLE;

    protected Map<UUID, CommandSource> byUUID = new HashMap<>();

    public CommandSource obtainSource(Audience sender) {
        if (sender instanceof Player) {
            return this.byUUID.computeIfAbsent(
                    ((Player) sender).uniqueId(),
                    ($) -> new SpongeCommandSource(sender)
            );
        }
        if (CONSOLE == null) {
            CONSOLE = new SpongeCommandSource(Sponge.game().server());
        }
        return CONSOLE;
    }

    public static class QuitListener {

        private final CommandSources sources;

        public QuitListener(CommandSources sources) {
            this.sources = sources;
        }

        @Listener
        public void onQuit(ServerSideConnectionEvent.Disconnect event) {
            sources.byUUID.remove(event.player().uniqueId());
        }

    }

}
