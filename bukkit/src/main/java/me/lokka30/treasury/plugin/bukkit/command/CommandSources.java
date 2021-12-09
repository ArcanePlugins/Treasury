/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.command;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CommandSources implements Listener {

    public static final CommandSource CONSOLE = new BukkitCommandSource(
            Bukkit.getConsoleSender()
    );

    private Map<UUID, CommandSource> byUUID = new HashMap<>();

    public CommandSource obtainSource(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return CONSOLE;
        }
        Player player = (Player) sender;
        if (!byUUID.containsKey(player.getUniqueId())) {
            byUUID.put(player.getUniqueId(), new BukkitCommandSource(player));
        }
        return byUUID.get(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        byUUID.remove(event.getPlayer().getUniqueId());
    }

}
