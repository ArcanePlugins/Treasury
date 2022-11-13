/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bungeecord;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CommandSources implements Listener {

    public static final CommandSource CONSOLE = new BungeeCommandSource(ProxyServer
            .getInstance()
            .getConsole());

    private Map<UUID, CommandSource> byUUID = new HashMap<>();

    public CommandSource obtainSource(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return byUUID.computeIfAbsent(
                    ((ProxiedPlayer) sender).getUniqueId(),
                    k -> new BungeeCommandSource(sender)
            );
        }
        return CONSOLE;
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        byUUID.remove(event.getPlayer().getUniqueId());
    }

}
