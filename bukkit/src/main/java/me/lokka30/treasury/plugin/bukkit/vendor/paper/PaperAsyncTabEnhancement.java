/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.paper;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.TreasuryBaseCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
