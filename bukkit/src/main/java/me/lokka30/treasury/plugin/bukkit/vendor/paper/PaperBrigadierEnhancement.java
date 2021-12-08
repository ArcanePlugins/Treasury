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

package me.lokka30.treasury.plugin.bukkit.vendor.paper;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommand;
import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Locale;

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

            for (String pluginRegistering : TreasuryPlugin.getInstance().pluginsListRegisteringProvider()) {
                if (pluginRegistering.toLowerCase(Locale.ROOT).startsWith(lastArg)) {
                    builder.suggest(pluginRegistering);
                }
            }

            return builder.buildFuture();
        };
    }
}
