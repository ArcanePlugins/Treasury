/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.lokka30.treasury.plugin.core.command.TreasuryBaseCommand;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.ArgumentReader;

public class TreasuryCommand implements Command.Raw {

    private final CommandSources sources;
    private final TreasuryBaseCommand base;

    private final Optional<Component> shortDescription;
    private final Component usage;

    public TreasuryCommand(CommandSources sources) {
        this.sources = sources;
        this.base = new TreasuryBaseCommand();

        this.shortDescription = Optional.of(Component.text("Manage the Treasury plugin"));
        this.usage = Component.text("/treasury");
    }

    @Override
    public CommandResult process(
            CommandCause cause, ArgumentReader.Mutable arguments
    ) {
        String[] args = arguments.input().split(" ");
        base.execute(this.sources.obtainSource(cause.audience()), "treasury", args);
        return CommandResult.success();
    }

    @Override
    public List<CommandCompletion> complete(
            CommandCause cause, ArgumentReader.Mutable arguments
    ) {
        String[] args = arguments.input().split(" ");
        List<CommandCompletion> ret = new ArrayList<>();
        List<String> stringCompletions = base.complete(
                this.sources.obtainSource(cause.audience()),
                "treasury",
                args
        );
        if (stringCompletions == null || stringCompletions.isEmpty()) {
            return ret;
        }
        for (String compl : stringCompletions) {
            ret.add(CommandCompletion.of(compl));
        }
        return ret;
    }

    @Override
    public boolean canExecute(CommandCause cause) {
        return true; // permission checks are run inside command execution
    }

    @Override
    public Optional<Component> shortDescription(CommandCause cause) {
        return this.shortDescription;
    }

    @Override
    public Optional<Component> extendedDescription(CommandCause cause) {
        return Optional.empty();
    }

    @Override
    public Component usage(CommandCause cause) {
        return this.usage;
    }

}
