/*
 * Copyright (c) 2021 lokka30.
 * This code is part of Treasury, an Economy API for Minecraft servers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You have received a copy of the GNU Affero General Public License
 * with this program - please see the LICENSE.md file. Alternatively,
 * please visit the <https://www.gnu.org/licenses/> website.
 *
 * Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 */

package me.lokka30.treasury.plugin.command;

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
