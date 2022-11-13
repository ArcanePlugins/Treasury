/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.velocity;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import java.util.Locale;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.TreasuryBaseCommand;

public class TreasuryCommand {

    public static void register(TreasuryVelocity plugin) {
        CommandSources sources = new CommandSources(plugin);
        plugin.getProxy().getEventManager().register(plugin, sources);
        TreasuryBaseCommand base = new TreasuryBaseCommand();
        plugin.getProxy().getCommandManager().register(new BrigadierCommand(buildLiteral(
                base,
                sources
        )));
    }

    //@formatter:off
    private static LiteralCommandNode<CommandSource> buildLiteral(
            TreasuryBaseCommand base,
            CommandSources sources
    ) {
        return LiteralArgumentBuilder.<CommandSource>literal("treasury")
                .requires(source -> source.hasPermission("treasury.command.treasury"))
                .executes(constructCommand(base, sources, ""))
                .then(
                        LiteralArgumentBuilder.<CommandSource>literal("help")
                                .requires(source -> source.hasPermission(
                                        "treasury.command.treasury.help"))
                                .executes(constructCommand(base, sources, "help"))
                )
                .then(
                        LiteralArgumentBuilder.<CommandSource>literal("info")
                                .requires(source -> source.hasPermission(
                                        "treasury.command.treasury.info"))
                                .executes(constructCommand(base, sources, "info"))
                )
                .then(
                        LiteralArgumentBuilder.<CommandSource>literal("reload")
                                .requires(source -> source.hasPermission(
                                        "treasury.command.treasury.reload"))
                                .executes(constructCommand(base, sources, "reload"))
                )
                .then(
                        LiteralArgumentBuilder.<CommandSource>literal("downloadLatest")
                                .requires(source -> source.hasPermission(
                                        "treasury.command.treasury.downloadLatest"))
                                .executes(constructCommand(base, sources, "downloadLatest"))
                )
                .then(
                        LiteralArgumentBuilder.<CommandSource>literal("economy")
                                .requires(source -> source.hasPermission(
                                        "treasury.command.treasury.economy"))
                                .executes(constructCommand(base, sources, "economy"))
                                .then(
                                        LiteralArgumentBuilder.<CommandSource>literal("info")
                                                .requires(source -> source.hasPermission(
                                                        "treasury.command.treasury.economy.info"))
                                                .executes(constructCommand(base, sources,
                                                        "economy info"))
                                )
                                .then(
                                        LiteralArgumentBuilder.<CommandSource>literal("help")
                                                .requires(source -> source.hasPermission(
                                                        "treasury.command.treasury.economy.help"))
                                                .executes(constructCommand(base, sources,
                                                        "economy help"))
                                )
                                .then(
                                        LiteralArgumentBuilder.<CommandSource>literal("migrate")
                                                .requires(source -> source.hasPermission(
                                                        "treasury.command.treasury.economy.migrate"))
                                                .executes(constructCommand(base, sources,
                                                        "economy migrate"))
                                                .then(
                                                        RequiredArgumentBuilder
                                                                .<CommandSource, String>argument(
                                                                        "plugin1",
                                                                        StringArgumentType.word()
                                                                )
                                                                .requires(source -> source.hasPermission(
                                                                        "treasury.command.treasury.migrate"))
                                                                .suggests(plugins())
                                                                .executes(context -> {
                                                                    String[] args = ("economy migrate " + context.getArgument(
                                                                            "plugin1",
                                                                            String.class
                                                                    )).split(" ");
                                                                    base.execute(
                                                                            sources.obtainSource(
                                                                                    context.getSource()),
                                                                            "treasury",
                                                                            args
                                                                    );
                                                                    return Command.SINGLE_SUCCESS;
                                                                })
                                                                .then(
                                                                        RequiredArgumentBuilder
                                                                                .<CommandSource, String>argument(
                                                                                        "plugin2",
                                                                                        StringArgumentType.word()
                                                                                )
                                                                                .requires(source -> source
                                                                                        .hasPermission(
                                                                                                "treasury.command.treasury.migrate"))
                                                                                .suggests(plugins())
                                                                                .executes(context -> {
                                                                                    String[] args = ("economy migrate " + context.getArgument(
                                                                                            "plugin1",
                                                                                            String.class
                                                                                    ) + " " + context.getArgument(
                                                                                            "plugin2",
                                                                                            String.class
                                                                                    )).split(" ");
                                                                                    base.execute(
                                                                                            sources.obtainSource(
                                                                                                    context.getSource()),
                                                                                            "treasury",
                                                                                            args
                                                                                    );
                                                                                    return Command.SINGLE_SUCCESS;
                                                                                })
                                                                )
                                                )
                                )
                ).build();
    }
    //@formatter:on

    private static Command<CommandSource> constructCommand(
            TreasuryBaseCommand base, CommandSources sources, String input
    ) {
        return (context) -> {
            String[] args = input.isEmpty() ? new String[0] : input.split(" ");
            base.execute(sources.obtainSource(context.getSource()), "treasury", args);
            return Command.SINGLE_SUCCESS;
        };
    }

    private static SuggestionProvider<CommandSource> plugins() {
        return (context, builder) -> {
            String lastArg = builder.getRemaining().toLowerCase(Locale.ROOT);

            for (String pluginRegistering : TreasuryPlugin
                    .getInstance()
                    .pluginsListRegisteringEconomyProvider()) {
                if (pluginRegistering.toLowerCase(Locale.ROOT).startsWith(lastArg)) {
                    builder.suggest(pluginRegistering);
                }
            }

            return builder.buildFuture();
        };
    }

}
