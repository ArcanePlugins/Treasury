package me.lokka30.treasury.plugin.core.command.subcommand;

import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.command.Subcommand;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import me.lokka30.treasury.plugin.core.utils.Utils;
import me.lokka30.treasury.plugin.core.utils.downloader.PluginDownloader;
import org.jetbrains.annotations.NotNull;

/**
 * A class, containing the logic of the "/treasury downloadLatest" command.
 *
 * @author MrIvanPlays
 */
public class DownloadLatestSubcommand implements Subcommand {

    @Override
    public void execute(
            @NotNull CommandSource source, @NotNull String label, @NotNull String[] args
    ) {
        if (!Utils.checkPermissionForCommand(source, "treasury.command.treasury.downloadLatest")) {
            return;
        }

        if (args.length != 0) {
            source.sendMessage(Message.of(
                    MessageKey.DOWNLOAD_LATEST_INVALID_USAGE,
                    MessagePlaceholder.placeholder("label", label)
            ));
            return;
        }

        source.sendMessage(Message.of(MessageKey.DOWNLOAD_LATEST_STARTED));
        QuickTimer timer = new QuickTimer();
        timer.start();
        if (!PluginDownloader.downloadLatest(source)) {
            return;
        }
        source.sendMessage(Message.of(
                MessageKey.DOWNLOAD_LATEST_SUCCESS,
                MessagePlaceholder.placeholder("time", timer.getTimer())
        ));
    }

}
