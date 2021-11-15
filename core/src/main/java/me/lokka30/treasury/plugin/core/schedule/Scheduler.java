package me.lokka30.treasury.plugin.core.schedule;

public interface Scheduler {

    void runSync(Runnable task);

    void runAsync(Runnable task);
}
