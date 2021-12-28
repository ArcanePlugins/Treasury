/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy;

import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.command.Subcommand;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import me.lokka30.treasury.plugin.core.utils.Utils;
import org.jetbrains.annotations.NotNull;

// "/treasury economy help"
public class EconomyHelpSub implements Subcommand {

    @Override
    public void execute(
            @NotNull final CommandSource source,
            @NotNull final String label,
            final @NotNull String[] args
    ) {
        if (!Utils.checkPermissionForCommand(source, "treasury.command.treasury.economy.help")) {
            return;
        }

        if (args.length != 0) {
            source.sendMessage(Message.of(
                    MessageKey.ECONOMY_HELP_INVALID_USAGE,
                    MessagePlaceholder.placeholder("label", label)
            ));
            return;
        }

        source.sendMessage(Message.of(MessageKey.ECONOMY_HELP_AVAILABLE_COMMANDS));
    }

}
