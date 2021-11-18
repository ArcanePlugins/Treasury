package me.lokka30.treasury.plugin.bukkit.fork.paper;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommand;
import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PaperBrigadierEnhancement implements Listener {

    @EventHandler
    public void onCommandRegister(CommandRegisteredEvent<BukkitBrigadierCommandSource> event) {
        String label = event.getCommandLabel();
        if (label.equalsIgnoreCase("treasury") || label.equalsIgnoreCase("treasury:treasury")) {
            event.setLiteral(buildLiteral(label, event.getBrigadierCommand()));
        }
    }

    private LiteralCommandNode<BukkitBrigadierCommandSource> buildLiteral(
            String label,
            BukkitBrigadierCommand<BukkitBrigadierCommandSource> command
    ) {
        return LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal(label)
                .executes(command)
                .then(
                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("help")
                                .requires(source -> source.getBukkitSender()
                                        .hasPermission("treasury.command.treasury.help"))
                                .executes(command)
                )
                .then(
                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("info")
                                .requires(source -> source.getBukkitSender()
                                        .hasPermission("treasury.command.treasury.info"))
                                .executes(command)
                )
                .then(
                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("reload")
                                .requires(source -> source.getBukkitSender()
                                        .hasPermission("treasury.command.treasury.reload"))
                                .executes(command)
                )
                .then(
                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("migrate")
                                .requires(source -> source.getBukkitSender()
                                        .hasPermission("treasury.command.treasury.migrate"))
                                .executes(command)
                                .then(
                                        RequiredArgumentBuilder.<BukkitBrigadierCommandSource, String>argument("plugin1", StringArgumentType.word())
                                                .requires(source -> source.getBukkitSender()
                                                        .hasPermission("treasury.command.treasury.migrate"))
                                                .suggests(plugins())
                                                .executes(command)
                                                .then(
                                                        RequiredArgumentBuilder.<BukkitBrigadierCommandSource, String>argument("plugin2", StringArgumentType.word())
                                                                .requires(source -> source.getBukkitSender()
                                                                        .hasPermission("treasury.command.treasury.migrate"))
                                                                .suggests(plugins())
                                                                .executes(command)
                                                )
                                )
                ).build();
    }

    private SuggestionProvider<BukkitBrigadierCommandSource> plugins() {
        return (context, builder) -> {
            String lastArg = builder.getRemainingLowerCase();

            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                if (plugin.getName().toLowerCase(Locale.ROOT).startsWith(lastArg)) {
                    builder.suggest(plugin.getName());
                }
            }

            return builder.buildFuture();
        };
    }
}
