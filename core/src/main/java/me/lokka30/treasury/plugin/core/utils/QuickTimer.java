package me.lokka30.treasury.plugin.core.utils;

/**
 * A timer to time how much an action runs.
 *
 * @author lokka30
 */
// copied from microlib
public class QuickTimer {

    private long startTime;

    public QuickTimer() {
        this.start();
    }

    public QuickTimer(long startTime) {
        this.startTime = startTime;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public long getTimer() {
        return System.currentTimeMillis() - this.startTime;
    }
}
