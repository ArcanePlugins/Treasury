package me.lokka30.treasury.plugin.core.command.subcommand.economy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.command.Subcommand;
import me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate.EconomyMigrateSub;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import me.lokka30.treasury.plugin.core.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class, containing the logic of "/treasury economy" command.
 *
 * @author MrIvanPlays
 */
public final class EconomySubcommand implements Subcommand {

    private Map<String, Subcommand> subcommands;

    public EconomySubcommand() {
        this.subcommands = new ConcurrentHashMap<>();
        registerSubcommand("info", new EconomyInfoSub());
        registerSubcommand("help", new EconomyHelpSub());
        registerSubcommand("migrate", new EconomyMigrateSub());
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

    @Override
    public void execute(@NotNull CommandSource sender, @NotNull String label, @NotNull String[] args) {
        if (!Utils.checkPermissionForCommand(sender, "treasury.command.treasury.economy")) {
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(
                    Message.of(MessageKey.ECONOMY_INVALID_USAGE_UNSPECIFIED, MessagePlaceholder.placeholder("label", label))
            );
            return;
        }
        Subcommand subcommand = subcommands.get(args[0]);
        if (subcommand == null) {
            sender.sendMessage(Message.of(
                            MessageKey.ECONOMY_INVALID_USAGE_SPECIFIED,
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
    public static final List<String> SUBCOMMAND_COMPLETIONS = Arrays.asList("info", "help", "migrate");

    @Nullable
    @Override
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
