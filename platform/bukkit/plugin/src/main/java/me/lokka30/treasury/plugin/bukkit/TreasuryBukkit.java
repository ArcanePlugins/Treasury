/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit;

import me.lokka30.treasury.plugin.bukkit.command.TreasuryCommand;
import me.lokka30.treasury.plugin.bukkit.vendor.BukkitVendor;
import me.lokka30.treasury.plugin.bukkit.vendor.paper.PaperEnhancements;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import me.lokka30.treasury.plugin.core.utils.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the plugin's main class, loaded by Bukkit's plugin manager.
 * It contains direct and indirect links to everything accessed within
 * the plugin.
 *
 * @author lokka30
 * @since v1.0.0
 */
public class TreasuryBukkit extends JavaPlugin {

    private BukkitTreasuryPlugin treasuryPlugin;

    /**
     * Run the start-up procedure for the plugin.
     * This is called by Bukkit's plugin manager.
     *
     * @author lokka30
     * @since v1.0.0
     */
    @Override
    public void onEnable() {
        final QuickTimer startupTimer = new QuickTimer();

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        treasuryPlugin = new BukkitTreasuryPlugin(this);
        TreasuryPlugin.setInstance(treasuryPlugin);
        treasuryPlugin.loadMessages();
        treasuryPlugin.loadSettings();
        TreasuryCommand.register(this);

        if (BukkitVendor.isPaper()) {
            PaperEnhancements.enhance(this);
        }

        UpdateChecker.checkForUpdates();
        new Metrics(this, 12927);

        treasuryPlugin.info("&fStart-up complete (took &b" + startupTimer.getTimer() + "ms&f).");
    }

    /**
     * Run the shut-down procedure for the plugin.
     * This is called by Bukkit's plugin manager.
     *
     * @author lokka30
     * @since v1.0.0
     */
    @Override
    public void onDisable() {
        final QuickTimer shutdownTimer = new QuickTimer();

        // Add onDisable code here if required.

        treasuryPlugin.info("&fShut-down complete (took &b" + shutdownTimer.getTimer() + "ms&f).");
    }

}
