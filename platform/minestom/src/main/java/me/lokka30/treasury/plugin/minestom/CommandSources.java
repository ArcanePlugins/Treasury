/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.minestom;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class CommandSources {

    public static final CommandSource CONSOLE = new MinestomCommandSource(MinecraftServer
            .getCommandManager()
            .getConsoleSender());

    private Map<UUID, CommandSource> byUUID = new HashMap<>();

    public CommandSources(TreasuryMinestom plugin) {
        plugin.getEventNode().addListener(
                PlayerDisconnectEvent.class,
                (e) -> byUUID.remove(e.getPlayer().getUuid())
        );
    }

    public CommandSource obtainSource(CommandSender sender) {
        if (sender instanceof Player) {
            return byUUID.computeIfAbsent(
                    ((Player) sender).getUuid(),
                    k -> new MinestomCommandSource(sender)
            );
        }
        return CONSOLE;
    }

}
