/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import me.lokka30.treasury.plugin.core.command.TreasuryBaseCommand;
import me.lokka30.treasury.plugin.core.command.subcommand.economy.EconomySubcommand;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class TreasuryCommand {

    private final CommandSources sources;
    private final TreasuryBaseCommand base;

    public TreasuryCommand(CommandSources sources) {
        this.sources = sources;
        this.base = new TreasuryBaseCommand();
    }

    public Command.Parameterized buildCommand() {
        Map<String, Command.Parameterized> temp = new HashMap<>();
        for (String sub : TreasuryBaseCommand.SUBCOMMAND_COMPLETIONS) {
            if (sub.equalsIgnoreCase("economy")) {
                continue;
            }
            Command.Parameterized cmd = Command
                    .builder()
                    .permission("treasury.command.treasury." + sub)
                    .shortDescription(Component.text("/treasury " + sub))
                    .executor(ctx -> this.execute(ctx, new String[]{sub}))
                    .terminal(true)
                    .build();
            temp.put(sub, cmd);
        }

        Command.Builder builder = Command
                .builder()
                .permission("treasury.command.treasury")
                .shortDescription(Component.text("Manage the Treasury plugin."))
                .terminal(true);
        builder.addChild(this.buildEconomy(), "economy");
        try {
            for (Map.Entry<String, Command.Parameterized> e : temp.entrySet()) {
                builder.addChild(e.getValue(), e.getKey());
            }
        } finally {
            temp.clear();
        }

        return builder.build();
    }

    private Command.Parameterized buildEconomy() {
        Map<String, Command.Parameterized> temp = new HashMap<>();
        for (String sub : EconomySubcommand.SUBCOMMAND_COMPLETIONS) {
            if (sub.equalsIgnoreCase("migrate")) {
                continue;
            }
            //@formatter:off
            Command.Parameterized cmd = Command
                    .builder()
                    .permission("treasury.command.treasury.economy." + sub)
                    .shortDescription(Component.text("/treasury economy " + sub))
                    .executor(ctx -> this.execute(ctx, new String[]{"economy", sub}))
                    .terminal(true)
                    .build();
            //@formatter:on
            temp.put(sub, cmd);
        }

        //@formatter:off
        Parameter.Value<String> plugin1Param;
        Parameter.Value<String> plugin2Param;
        Command.Parameterized migrate = Command.builder()
                .permission("treasury.command.treasury.economy.migrate")
                .shortDescription(Component.text("/treasury economy migrate <plugin1> <plugin2>"))
                .addParameters(
                        plugin1Param = Parameter
                                .string()
                                .key("plugin1")
                                .completer((context, input) -> this.complete(
                                        context,
                                        new String[] {"economy", "migrate", input}
                                )).build(),
                        plugin2Param = Parameter
                                .string()
                                .key("plugin2")
                                .completer((context, input) -> this.complete(
                                        context,
                                        new String[] {"economy", "migrate", input}
                                ))
                                .build()
                )
                .executor(ctx -> {
                    Optional<String> firstPlugin = ctx.one(plugin1Param);
                    Optional<String> secondPlugin = ctx.one(plugin2Param);
                    if (!firstPlugin.isPresent() || !secondPlugin.isPresent()) {
                        // sends usage
                        return this.execute(ctx, new String[] {"economy", "migrate"});
                    }
                    return this.execute(
                            ctx,
                            new String[] {"economy", "migrate", firstPlugin.get(), secondPlugin.get()}
                    );
                })
                .terminal(true)
                .build();
        //@formatter:on

        Command.Builder builder = Command
                .builder()
                .permission("treasury.command.treasury.economy")
                .shortDescription(Component.text("/treasury economy <sub> [args]"))
                .terminal(true);
        builder.addChild(migrate, "migrate");
        try {
            for (Map.Entry<String, Command.Parameterized> e : temp.entrySet()) {
                builder.addChild(e.getValue(), e.getKey());
            }
        } finally {
            temp.clear();
        }

        return builder.build();
    }

    private CommandResult execute(CommandContext context, String[] args) {
        this.base.execute(this.sources.obtainSource(context.cause().audience()), "treasury", args);
        return CommandResult.success();
    }

    private List<CommandCompletion> complete(CommandContext context, String[] args) {
        List<String> completions = this.base.complete(this.sources.obtainSource(context
                .cause()
                .audience()), "treasury", args);

        if (completions == null || completions.isEmpty()) {
            return Collections.emptyList();
        }

        List<CommandCompletion> ret = new ArrayList<>();
        for (String comp : completions) {
            ret.add(CommandCompletion.of(comp));
        }
        return ret;
    }

}
