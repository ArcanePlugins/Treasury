/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand;

import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.command.Subcommand;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import me.lokka30.treasury.plugin.core.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class ReloadSubcommand implements Subcommand {

    /*
    inf: View the plugin's available commands.
    cmd: /treasury reload
    arg:         |      0
    len:         0      1
     */

    @Override
    public void execute(@NotNull CommandSource sender, @NotNull String label, @NotNull String[] args) {
        if (!Utils.checkPermissionForCommand(sender, "treasury.command.treasury.reload")) {
            return;
        }

        if (args.length != 0) {
            sender.sendMessage(
                    Message.of(MessageKey.RELOAD_INVALID_USAGE, MessagePlaceholder.placeholder("label", label))
            );
            return;
        }

        sender.sendMessage(Message.of(MessageKey.RELOAD_START));

        final QuickTimer timer = new QuickTimer();

        TreasuryPlugin.getInstance().reload();

        sender.sendMessage(
                Message.of(MessageKey.RELOAD_COMPLETE, MessagePlaceholder.placeholder("time", timer.getTimer()))
        );
    }

}
