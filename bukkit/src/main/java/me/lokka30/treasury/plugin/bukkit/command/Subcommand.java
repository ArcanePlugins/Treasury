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

package me.lokka30.treasury.plugin.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author lokka30
 * @since v1.0.0
 * A subcommand is a class handling the execution of
 * a specific part of a command, which in turn makes
 * the code cleaner and easier to understand.
 * For example, '/example create' and '/example delete'
 * can be their own separate subcommands 'create' and
 * 'delete', to avoid cluttering the base command's
 * executor class.
 */
public interface Subcommand {

    /**
     * @author lokka30
     * @since v1.0.0
     * @see org.bukkit.command.TabExecutor
     * Run the code executing the subcommand.
     * @param sender who ran the subcommand.
     * @param label of the base command (alias).
     * @param args specified by the sender, INCLUDING the subcommand's argument.
     */
    void run(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String[] args);

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Collections#emptyList()
     * @see org.bukkit.command.TabExecutor#onTabComplete(CommandSender, Command, String, String[])
     * Get a list of suggestions to return for 1.13+'s tab completion system.
     * @param sender who will receive the suggestions.
     * @param label of the base command (alias).
     * @param args specified by the sender, INCLUDING the subcommand's argument.
     * @return a list of compiled suggestions - or, if none, `Collections.emptyList()` is returned.
     */
    @SuppressWarnings("unused")
    @NotNull
    default List<String> getTabSuggestions(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String[] args) {
        return Collections.emptyList();
    }

}
