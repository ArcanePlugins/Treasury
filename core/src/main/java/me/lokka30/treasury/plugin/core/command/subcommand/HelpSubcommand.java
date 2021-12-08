/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand;

import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.command.Subcommand;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import me.lokka30.treasury.plugin.core.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class HelpSubcommand implements Subcommand {

    /*
    inf: View the plugin's available commands.
    cmd: /treasury help
    arg:         |    0
    len:         0    1
     */

    @Override
    public void execute(@NotNull CommandSource sender, @NotNull String label, @NotNull String[] args) {
        if (!Utils.checkPermissionForCommand(sender, "treasury.command.treasury.help")) {
            return;
        }

        if (args.length != 0) {
            sender.sendMessage(
                    Message.of(MessageKey.HELP_INVALID_USAGE, MessagePlaceholder.placeholder("label", label))
            );
            return;
        }

        sender.sendMessage(Message.of(MessageKey.HELP_AVAILABLE_COMMANDS));
    }
}
