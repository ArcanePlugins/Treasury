/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.folia;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import org.jetbrains.annotations.NotNull;

public class FoliaScheduler implements Scheduler {

    private final TreasuryBukkit plugin;

    private Map<Integer, io.papermc.paper.threadedregions.scheduler.ScheduledTask> tasks = new ConcurrentHashMap<>();
    private final Lock tasksLock = new ReentrantLock();

    public FoliaScheduler(@NotNull TreasuryBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runSync(final Runnable task) {
        plugin.getServer().getGlobalRegionScheduler().run(plugin, ($) -> task.run());
    }

    @Override
    public int runAsync(final Runnable task) {
        int id = ThreadLocalRandom.current().nextInt(100000);
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask = plugin
                .getServer()
                .getAsyncScheduler()
                .runNow(plugin, ($) -> task.run());
        tasksLock.lock();
        try {
            tasks.put(id, scheduledTask);
        } finally {
            tasksLock.unlock();
        }
        return id;
    }

    @Override
    public void cancelTask(final int id) {
        tasksLock.lock();
        try {
            io.papermc.paper.threadedregions.scheduler.ScheduledTask task = tasks.get(id);
            if (task != null) {
                task.cancel();
                tasks.remove(id);
            }
        } finally {
            tasksLock.unlock();
        }
    }

}
