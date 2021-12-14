/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import me.lokka30.treasury.plugin.core.command.subcommand.HelpSubcommand;
import me.lokka30.treasury.plugin.core.command.subcommand.InfoSubcommand;
import me.lokka30.treasury.plugin.core.command.subcommand.ReloadSubcommand;
import me.lokka30.treasury.plugin.core.command.subcommand.economy.EconomySubcommand;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import me.lokka30.treasury.plugin.core.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class, containing the logic of the treasury command.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public final class TreasuryBaseCommand {

    private Map<String, Subcommand> subcommands;

    public TreasuryBaseCommand() {
        this.subcommands = new HashMap<>();
        registerSubcommand("help", new HelpSubcommand());
        registerSubcommand("info", new InfoSubcommand());
        registerSubcommand("reload", new ReloadSubcommand());
        registerSubcommand("economy", new EconomySubcommand());
    }

    /**
     * Registers a new subcommand to handle.
     *
     * @param name       subcommand name
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
     * @param label  command label
     * @param args   command args
     */
    public void execute(@NotNull CommandSource sender, @NotNull String label, @NotNull String[] args) {
        if (!Utils.checkPermissionForCommand(sender, "treasury.command.treasury")) {
            return;
        }

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
                            MessagePlaceholder.placeholder("subcommand", args[0])
                    )
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
    public static final List<String> SUBCOMMAND_COMPLETIONS = Arrays.asList("help", "info", "reload", "economy");

    /**
     * Runs completions for the base /treasury command.
     *
     * @param sender who asked for tab completions
     * @param label  command label
     * @param args   command arguments
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
