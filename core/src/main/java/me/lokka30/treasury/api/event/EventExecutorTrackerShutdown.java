/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.event;

public class EventExecutorTrackerShutdown {

    public static void shutdown() {
        EventExecutorTracker.INSTANCE.shutdown();
    }

}
