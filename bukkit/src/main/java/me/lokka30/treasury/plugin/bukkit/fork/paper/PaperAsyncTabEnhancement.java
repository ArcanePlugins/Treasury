package me.lokka30.treasury.plugin.bukkit.fork.paper;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PaperAsyncTabEnhancement implements Listener {

    @EventHandler
    public void onAsyncTab(AsyncTabCompleteEvent event) {
        String buffer = event.getBuffer();
        if ((!event.isCommand() && !buffer.startsWith("/")) || buffer.indexOf(' ') == -1) {
            return;
        }

        String[] parts = buffer.split(" ");
        if (!parts[0].equalsIgnoreCase("/treasury") && !parts[0].equalsIgnoreCase("/treasury:treasury")) {
            return;
        }
        if (parts[1] != null) {
            String subcommand = parts[1];
            if (subcommand.equalsIgnoreCase("migrate")) {
                if (parts.length > 4) {
                    return;
                }
                event.setCompletions(getCompletions(parts[parts.length - 1].toLowerCase(Locale.ROOT)));
                event.setHandled(true);
                return;
            }
            if (parts.length > 2) {
                event.setCompletions(Collections.emptyList());
                event.setHandled(true);
            }
        }
    }

    private List<String> getCompletions(String lastArg) {
        return Arrays.stream(
                Bukkit.getPluginManager().getPlugins()
        ).map(Plugin::getName)
                .filter(name -> lastArg.startsWith(name.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }
}
