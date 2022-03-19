/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.lokka30.treasury.plugin.core.command.CommandSource;

public class CommandSources {

    private Map<UUID, CommandSource> byUUID = new HashMap<>();
    private final CommandSource console;

    public CommandSources(TreasuryVelocity plugin) {
        this.console = new VelocityCommandSource(plugin.getProxy().getConsoleCommandSource());
    }

    public CommandSource obtainSource(com.velocitypowered.api.command.CommandSource sender) {
        if (sender instanceof Player) {
            return byUUID.computeIfAbsent(
                    ((Player) sender).getUniqueId(),
                    k -> new VelocityCommandSource(sender)
            );
        }
        return console;
    }

    @Subscribe
    public void onQuit(DisconnectEvent event) {
        byUUID.remove(event.getPlayer().getUniqueId());
    }

}
