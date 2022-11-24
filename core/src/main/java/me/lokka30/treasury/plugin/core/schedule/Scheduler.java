/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.schedule;

/**
 * Represents a scheduler wrapper.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public interface Scheduler {

    /**
     * Runs the specified task synchronously (on the main thread of the platform implemented)
     *
     * @param task the task you want to run
     */
    void runSync(Runnable task);

    /**
     * Runs the specified task asynchronously.
     *
     * @param task the task you want to run
     */
    void runAsync(Runnable task);

}
