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

package me.lokka30.treasury.plugin.core.command.subcommand;

import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.command.Subcommand;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public class ReloadSubcommand implements Subcommand {

    /*
    inf: View the plugin's available commands.
    cmd: /treasury reload
    arg:         |      0
    len:         0      1
     */

    @NotNull private final Treasury main;
    public ReloadSubcommand(@NotNull final Treasury main) { this.main = main; }

    @Override
    public boolean execute(@NotNull CommandSource sender, @NotNull String label, @NotNull String[] args) {
        if(!Utils.checkPermissionForCommand(main, sender, "treasury.command.treasury.reload")) return;

        if(args.length != 1) {
            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.reload.invalid-usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ));
            return;
        }

        new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.reload.reload-start"), Collections.singletonList(
                new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true)
        ));

        final QuickTimer timer = new QuickTimer();

        main.fileHandler.loadFiles();
        main.debugHandler.loadEnabledCategories();

        new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.reload.reload-complete"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                new MultiMessage.Placeholder("time", timer.getTimer() + "", false)
        ));
    }
}
