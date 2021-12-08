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

package me.lokka30.treasury.plugin.core.command;

import me.lokka30.treasury.plugin.core.command.subcommand.HelpSubcommand;
import me.lokka30.treasury.plugin.core.command.subcommand.InfoSubcommand;
import me.lokka30.treasury.plugin.core.command.subcommand.ReloadSubcommand;
import me.lokka30.treasury.plugin.core.command.subcommand.migrate.MigrateSubcommand;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A class, containing the logic of the treasury command.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public final class TreasuryBaseCommand {

    private final Map<String, Subcommand> subcommands;

    public TreasuryBaseCommand() {
        this.subcommands = new HashMap<>();
        registerSubcommand("help", new HelpSubcommand());
        registerSubcommand("info", new InfoSubcommand());
        registerSubcommand("reload", new ReloadSubcommand());
        registerSubcommand("migrate", new MigrateSubcommand());
    }

    /**
     * Registers a new subcommand to handle.
     *
     * @param name subcommand name
     * @param subcommand subcommand
     */
    private void registerSubcommand(@NotNull String name, @NotNull Subcommand subcommand) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(subcommand, "subcommand");
        if (subcommands.containsKey(name)) {
            subcommands.replace(name, subcommand);
        } else {
            subcommands.put(name, subcommand);
        }
    }

    /**
     * Executes the base /treasury command.
     *
     * @param sender who ran the command
     * @param label command label
     * @param args command args
     */
    public void execute(@NotNull CommandSource sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(
                    Message.of(MessageKey.INVALID_USAGE_UNSPECIFIED, MessagePlaceholder.placeholder("label", label))
            );
            return;
        }
        Subcommand subcommand = subcommands.get(args[0]);
        if (subcommand == null) {
            sender.sendMessage(Message.of(
                    MessageKey.INVALID_USAGE_SPECIFIED,
                    MessagePlaceholder.placeholder("label", label),
                    MessagePlaceholder.placeholder("subcommand", args[0]))
            );
            return;
        }
        subcommand.execute(
                sender,
                label,
                args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length)
        );
    }

    @NotNull
    public static final List<String> SUBCOMMAND_COMPLETIONS = Arrays.asList("help", "info", "migrate", "reload");

    /**
     * Runs completions for the base /treasury command.
     *
     * @param sender who asked for tab completions
     * @param label command label
     * @param args command arguments
     * @return list with completions, can be null
     */
    @Nullable
    public List<String> complete(@NotNull CommandSource sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return Collections.emptyList();
        } else if (args.length == 1) {
            return SUBCOMMAND_COMPLETIONS.stream()
                    .filter(c -> c.startsWith(args[0].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        } else {
            Subcommand subcommand = subcommands.get(args[0]);
            if (subcommand == null) {
                return Collections.emptyList();
            }
            return subcommand.complete(sender, label, Arrays.copyOfRange(args, 1, args.length));
        }
    }
}
