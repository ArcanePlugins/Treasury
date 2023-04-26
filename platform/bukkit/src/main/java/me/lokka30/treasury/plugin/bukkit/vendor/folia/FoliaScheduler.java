/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.folia;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import org.jetbrains.annotations.NotNull;

public class FoliaScheduler implements Scheduler {

    private final TreasuryBukkit plugin;

    public FoliaScheduler(@NotNull TreasuryBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runSync(final Runnable task) {
        plugin.getServer().getGlobalRegionScheduler().run(plugin, ($) -> task.run());
    }

    @Override
    public void runAsync(final Runnable task) {
        plugin.getServer().getAsyncScheduler().runNow(plugin, ($) -> task.run());
    }

}
