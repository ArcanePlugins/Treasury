/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.minestom;

import java.util.List;
import me.lokka30.treasury.plugin.core.command.TreasuryBaseCommand;
import me.lokka30.treasury.plugin.core.command.subcommand.economy.EconomySubcommand;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionCallback;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

public class TreasuryCommand extends Command {

    final CommandSources sources;
    final TreasuryBaseCommand baseCommand;

    public TreasuryCommand(TreasuryMinestom plugin) {
        super("treasury", (String[]) null);

        sources = new CommandSources(plugin);
        baseCommand = new TreasuryBaseCommand();

        setDefaultExecutor((sender, context) -> {
            String[] args = context.getInput().replace(context.getCommandName(), "").trim().split(
                    " ");

            baseCommand.execute(sources.obtainSource(sender), "treasury", args);
        });

        setCondition((sender, input) -> {
            if (input == null) {
                return sender.hasPermission("treasury.command.treasury");
            }
            String replacedInput = input.replace("treasury", "");
            String permissionToCheck = null;
            if (replacedInput.isEmpty()) {
                permissionToCheck = "treasury.command.treasury";
            } else {
                String[] args = replacedInput.trim().split(" ");
                if (args.length == 1) {
                    String sub = args[0];
                    if (TreasuryBaseCommand.SUBCOMMAND_COMPLETIONS.contains(sub)) {
                        permissionToCheck = "treasury.command.treasury." + sub;
                    }
                } else if (args.length == 2) {
                    String firstSub = args[0];
                    if (firstSub.equalsIgnoreCase("economy")) {
                        String secondSub = args[1];
                        if (EconomySubcommand.SUBCOMMAND_COMPLETIONS.contains(secondSub)) {
                            permissionToCheck = "treasury.command.treasury.economy." + secondSub;
                        }
                    }
                } else if (args.length > 2) {
                    if (args[0].equalsIgnoreCase("economy") && args[1].equalsIgnoreCase("migrate")) {
                        permissionToCheck = "treasury.command.treasury.economy.migrate";
                    }
                }
            }

            return permissionToCheck != null && sender.hasPermission(permissionToCheck);
        });

        this.addLiterals();
    }

    private void addLiterals() {
        for (String compl : TreasuryBaseCommand.SUBCOMMAND_COMPLETIONS) {
            if (compl.equalsIgnoreCase("economy")) {
                continue;
            }
            ArgumentLiteral literal = ArgumentType.Literal(compl);
            this.addSyntax(this.getDefaultExecutor(), literal);
        }

        this.addSubcommand(new EconomySub(this));
    }

    private static class EconomySub extends Command {

        public EconomySub(TreasuryCommand parent) {
            super("economy", (String[]) null);

            setDefaultExecutor(parent.getDefaultExecutor());
            setCondition(parent.getCondition());

            for (String compl : EconomySubcommand.SUBCOMMAND_COMPLETIONS) {
                if (compl.equalsIgnoreCase("migrate")) {
                    continue;
                }
                ArgumentLiteral literal = ArgumentType.Literal(compl);
                this.addSyntax(this.getDefaultExecutor(), literal);
            }

            this.addSyntax(
                    this.getDefaultExecutor(),
                    ArgumentType.Literal("migrate"),
                    ArgumentType
                            .String("plugin1")
                            .setSuggestionCallback(this.getSuggestionCallback(parent)),
                    ArgumentType
                            .String("plugin2")
                            .setSuggestionCallback(this.getSuggestionCallback(parent))
            );
        }

        private SuggestionCallback getSuggestionCallback(TreasuryCommand parent) {
            return (sender, context, suggestion) -> {
                String[] args = context
                        .getInput()
                        .replace(context.getCommandName(), "")
                        .trim()
                        .split(" ");

                List<String> completions = parent.baseCommand.complete(parent.sources.obtainSource(
                        sender), "treasury", args);

                if (completions == null || completions.isEmpty()) {
                    return;
                }

                for (String completion : completions) {
                    suggestion.addEntry(new SuggestionEntry(completion));
                }
            };
        }

    }

}
