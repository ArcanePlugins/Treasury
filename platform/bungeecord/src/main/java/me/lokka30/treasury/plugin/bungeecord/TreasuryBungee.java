/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bungeecord;

import me.lokka30.treasury.api.common.event.EventExecutorTrackerShutdown;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import me.lokka30.treasury.plugin.core.utils.UpdateChecker;
import net.md_5.bungee.api.plugin.Plugin;

public class TreasuryBungee extends Plugin {

    private BungeeTreasuryPlugin treasuryPlugin;

    // onLoad instead of onEnable because in bungee you can't specify when your plugin loads
    // (except of course, if you have dependencies, then your plugin will load after the
    // dependencies)
    @Override
    public void onLoad() {
        final QuickTimer startupTimer = new QuickTimer();

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        treasuryPlugin = new BungeeTreasuryPlugin(this);
        TreasuryPlugin.setInstance(treasuryPlugin);
        treasuryPlugin.loadMessages();
        treasuryPlugin.loadSettings();
        TreasuryCommand.register(this);

        UpdateChecker.checkForUpdates();

        treasuryPlugin.info("&fStart-up complete (took &b" + startupTimer.getTimer() + "ms&f)");
    }

    @Override
    public void onDisable() {
        final QuickTimer shutdownTimer = new QuickTimer();

        // make sure to clean up even though we don't have service migration from other service
        // manager to ours as bukkit impl does. that's not really necessary, but it doesn't hurt
        // to be here
        ServiceRegistry.INSTANCE.unregisterAll("Treasury");

        // Shutdown events
        EventExecutorTrackerShutdown.shutdown();

        treasuryPlugin.info("&fShut-down complete (took &b" + shutdownTimer.getTimer() + "ms&f).");
    }

}
