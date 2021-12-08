/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.utils;

/**
 * This is a small class useful for timing simple things such as the time required to start-up a plugin or run a command.
 * <p>
 * Mark the starting point of the timer with `QuickTimer timer = new QuickTimer()`, then get the time (in milliseconds)
 * since it started using `QuickTimer#getTimer()`.
 *
 * @author lokka30
 * @see System#currentTimeMillis()
 * @since v1.0.0
 */
@SuppressWarnings("unused")
public class QuickTimer {

    private long startTime;

    public QuickTimer() {
        start();
    }

    public QuickTimer(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Re/start the timer.
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }

    /**
     * @return time (millis) since start time
     */
    public long getTimer() {
        return System.currentTimeMillis() - startTime;
    }
}
