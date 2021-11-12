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

package me.lokka30.treasury.plugin.bukkit.command;

import me.lokka30.treasury.plugin.bukkit.Treasury;
import me.lokka30.treasury.plugin.bukkit.command.treasury.TreasuryCommand;
import me.lokka30.treasury.plugin.bukkit.misc.Utils;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

public class CommandHandler {

    @NotNull private final Treasury main;

    public CommandHandler(@NotNull final Treasury main) { this.main = main; }

    /**
     * @author lokka30
     * @since v1.0.0
     * @see CommandHandler#registerCommand(String, TabExecutor)
     * Registers all commands from Treasury.
     */
    public void registerCommands() {
        Utils.logger.info("Registering commands...");

        registerCommand("treasury", new TreasuryCommand(main));
    }

    /**
     * @author lokka30
     * @since v1.0.0
     * Attempts to register the specified command.
     * If unsuccessful, console is notified.
     * The base command is probably case-sensitive.
     * @param baseCommand defined in plugin.yml.
     * @param executor handling the execution of the command.
     */
    private void registerCommand(@SuppressWarnings("SameParameterValue") @NotNull final String baseCommand, @NotNull final TabExecutor executor) {
        assert !baseCommand.isEmpty();

        final PluginCommand pluginCommand = main.getCommand(baseCommand);
        if(pluginCommand == null) {
            Utils.logger.error("Unable to register command '&b/" + baseCommand + "&7'. Please notify a Treasury developer.");
        } else {
            pluginCommand.setExecutor(executor);
            Utils.logger.info("Registered command '&b/" + baseCommand + "&7' successfully.");
        }
    }
}
