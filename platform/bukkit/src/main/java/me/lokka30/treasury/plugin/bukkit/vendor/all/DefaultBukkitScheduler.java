/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.all;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import org.bukkit.scheduler.BukkitTask;
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
    public int runAsync(final Runnable task) {
        BukkitTask bukkit = plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task);
        return bukkit.getTaskId();
    }

    @Override
    public void cancelTask(final int id) {
        plugin.getServer().getScheduler().cancelTask(id);
    }

}
