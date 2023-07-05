/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.all;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import org.jetbrains.annotations.NotNull;

public class DefaultBukkitScheduler implements Scheduler {

    private final TreasuryBukkit plugin;

    public DefaultBukkitScheduler(@NotNull TreasuryBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runSync(final Runnable task) {
        plugin.getServer().getScheduler().runTask(plugin, task);
    }

    @Override
    public void runAsync(final Runnable task) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task);
    }

}
