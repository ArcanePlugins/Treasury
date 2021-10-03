/*
 * Copyright (c) 2021-2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.command.treasury.subcommand;

import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.command.Subcommand;
import me.lokka30.treasury.plugin.misc.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpSubcommand implements Subcommand {

    /*
    inf: View the plugin's available commands.
    cmd: /treasury help
    arg:         |    0
    len:         0    1
     */

    @NotNull private final Treasury main;
    public HelpSubcommand(@NotNull final Treasury main) { this.main = main; }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(!Utils.checkPermissionForCommand(main, sender, "treasury.command.treasury.help")) return;

        if(args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid usage, try '/" + label + " help'.");
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "Available commands:");
        sender.sendMessage(ChatColor.GRAY + " -> /" + label + " help - view a list of Treasury's commands");
        sender.sendMessage(ChatColor.GRAY + " -> /" + label + " info - view info about Treasury and the Provider");
        sender.sendMessage(ChatColor.GRAY + " -> /" + label + " migrate - migrate from one Provider to another");
    }
}
