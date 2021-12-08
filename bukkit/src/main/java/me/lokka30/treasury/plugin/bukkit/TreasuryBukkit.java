/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit;

import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.plugin.bukkit.command.TreasuryCommand;
import me.lokka30.treasury.plugin.bukkit.vendor.BukkitVendor;
import me.lokka30.treasury.plugin.bukkit.vendor.paper.PaperEnhancements;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import me.lokka30.treasury.plugin.core.utils.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author lokka30
 * @since v1.0.0
 * This is the plugin's main class, loaded by Bukkit's plugin manager.
 * It contains direct and indirect links to everything accessed within
 * the plugin.
 */
@SuppressWarnings("unused")
public class TreasuryBukkit extends JavaPlugin {

    /**
     * This is Treasury's API version. (Not the same as api-version from plugin.yml!)
     * Any major changes to the API should make this number increase.
     * This allows Treasury to warn server owners if their Provider
     * does not support the latest Treasury API version.
     */
    @NotNull public static final EconomyAPIVersion ECONOMY_API_VERSION = EconomyAPIVersion.VERSION_1;

    private BukkitTreasuryPlugin treasuryPlugin;

    /**
     * @author lokka30
     * @since v1.0.0
     * Run the start-up procedure for the plugin.
     * This is called by Bukkit's plugin manager.
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
     * @author lokka30
     * @since v1.0.0
     * Run the shut-down procedure for the plugin.
     * This is called by Bukkit's plugin manager.
     */
    @Override
    public void onDisable() {
        final QuickTimer shutdownTimer = new QuickTimer();

        // Add onDisable code here if required.

        treasuryPlugin.info("&fShut-down complete (took &b" + shutdownTimer.getTimer() + "ms&f).");
    }
}
