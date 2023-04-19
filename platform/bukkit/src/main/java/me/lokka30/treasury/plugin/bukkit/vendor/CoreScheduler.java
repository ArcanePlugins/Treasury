/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.bukkit.vendor.all.DefaultBukkitScheduler;
import me.lokka30.treasury.plugin.bukkit.vendor.folia.FoliaScheduler;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class CoreScheduler {

    private final Scheduler schedulerImpl;

    public CoreScheduler(@NotNull TreasuryBukkit plugin) {
        this.schedulerImpl = BukkitVendor.isFolia()
                ? new FoliaScheduler(plugin)
                : new DefaultBukkitScheduler(plugin);
    }

    @Contract(pure = true)
    @NotNull
    public Scheduler getImpl() {
        return schedulerImpl;
    }

}
