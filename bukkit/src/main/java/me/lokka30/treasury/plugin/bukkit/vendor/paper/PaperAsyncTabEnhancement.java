/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.paper;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.TreasuryBaseCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
            } else {
                event.setCompletions(
                        TreasuryBaseCommand.SUBCOMMAND_COMPLETIONS.stream()
                                .filter(s -> s.startsWith(subcommand.toLowerCase(Locale.ROOT)))
                                .collect(Collectors.toList())
                );
            }
            event.setHandled(true);
        }
    }

    private List<String> getCompletions(String lastArg) {
        return TreasuryPlugin.getInstance().pluginsListRegisteringProvider()
                .stream()
                .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(lastArg))
                .collect(Collectors.toList());
    }
}
