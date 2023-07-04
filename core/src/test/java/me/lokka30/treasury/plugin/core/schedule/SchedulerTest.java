/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.schedule;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SchedulerTest {

    static class SimpleImpl implements Scheduler {

        @Override
        public void runSync(final Runnable task) {
            // we don't need this for testing purposes
        }

        @Override
        public int runAsync(final Runnable task) {
            ForkJoinPool.commonPool().execute(task);
            return -1; // don't care for test purposes
        }

        @Override
        public void cancelTask(final int id) {
            // don't need this for testing purposes
        }

    }

    private static Scheduler scheduler;

    @BeforeAll
    static void initialize() {
        scheduler = new SimpleImpl();
    }

    @AfterAll
    static void terminate() {
        scheduler = null;
    }

    @Test
    void testAsyncSchedule() {
        ThreadNameCatcher threadNameCatcher = new ThreadNameCatcher();
        threadNameCatcher.catchName(ThreadNameCatcher.Identifier.MAIN);
        Scheduler.Scheduled task = scheduler.runAsync(() -> {
            threadNameCatcher.catchName(ThreadNameCatcher.Identifier.ASYNC);
            System.out.println("yeet from " + Thread.currentThread().getName());
        }, 100, 100, TimeUnit.MILLISECONDS);
        threadNameCatcher.whenAsyncCaught(task::cancel);
        Assertions.assertEquals(2, threadNameCatcher.names.size());
        Assertions.assertNotEquals(
                threadNameCatcher.names.get(ThreadNameCatcher.Identifier.MAIN),
                threadNameCatcher.names.get(ThreadNameCatcher.Identifier.ASYNC)
        );
    }

}
