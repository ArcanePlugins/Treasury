/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

public class EventExecutorHolderShutdown {

    public static void shutdown() {
        ExecutorHolder.INSTANCE.shutdown();
    }

}
