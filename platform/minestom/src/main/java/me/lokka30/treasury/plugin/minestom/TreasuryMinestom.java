/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.minestom;

import java.io.IOException;
import java.nio.file.Files;
import me.lokka30.treasury.api.common.event.EventExecutorTrackerShutdown;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import me.lokka30.treasury.plugin.core.utils.UpdateChecker;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;

public class TreasuryMinestom extends Extension {

    private MinestomTreasuryPlugin treasuryPlugin;

    @Override
    public void initialize() {
        final QuickTimer startupTimer = new QuickTimer();

        if (!Files.exists(getDataDirectory())) {
            try {
                Files.createDirectories(getDataDirectory());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        treasuryPlugin = new MinestomTreasuryPlugin(this);
        TreasuryPlugin.setInstance(treasuryPlugin);
        treasuryPlugin.loadMessages();
        treasuryPlugin.loadSettings();
        MinecraftServer.getCommandManager().register(new TreasuryCommand(this));

        UpdateChecker.checkForUpdates();

        treasuryPlugin.logStartupMessage(startupTimer, false);
    }

    @Override
    public void terminate() {
        treasuryPlugin.shutdown(false);
    }

}
