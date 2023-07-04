/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.schedule;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
     * @return task id
     */
    int runAsync(Runnable task);

    void cancelTask(int id);

    default Scheduled runAsync(
            Runnable task, long delay, long repeat, TimeUnit unit
    ) {
        Scheduled scheduled = new Scheduled() {

            private AtomicBoolean cancelled = new AtomicBoolean(false);
            private AtomicBoolean done = new AtomicBoolean(false);

            private long delayInner = delay;
            private long repeatInner = repeat;
            private TimeUnit unitInner = unit;

            private int taskId;

            @Override
            public void start(long delayNew, long repeatNew, TimeUnit unitNew) {
                if (cancelled.get()) {
                    cancelled.set(false);
                }
                if (done.get()) {
                    done.set(false);
                }
                this.delayInner = delayNew;
                this.repeatInner = repeatNew;
                this.unitInner = unitNew;
                this.run();
            }

            @Override
            public boolean cancelled() {
                return cancelled.get();
            }

            @Override
            public boolean done() {
                return done.get() || cancelled.get();
            }

            @Override
            public void cancel() {
                cancelled.set(true);
                cancelTask(taskId);
            }

            @Override
            public void run() {
                if (cancelled.get()) {
                    return;
                }
                this.taskId = runAsync(() -> {
                    if (delayInner > 0) {
                        try {
                            Thread.sleep(unitInner.toMillis(delayInner));
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    while (!cancelled.get()) {
                        task.run();
                        if (repeatInner <= 0) {
                            done.set(true);
                            break;
                        }
                        try {
                            Thread.sleep(unitInner.toMillis(repeatInner));
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });
            }
        };
        scheduled.run();
        return scheduled;
    }

    interface Scheduled extends Runnable {

        void start(long delay, long repeat, TimeUnit unit);

        boolean cancelled();

        boolean done();

        void cancel();

    }

    abstract class ScheduledTask implements Scheduled {

        private final Scheduler scheduler;
        private Scheduled parent;

        public ScheduledTask(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        @Override
        public void start(long delay, long repeat, TimeUnit unit) {
            if (this.parent != null) {
                this.parent.start(delay, repeat, unit);
                return;
            }
            this.parent = scheduler.runAsync(this, delay, repeat, unit);
        }

        @Override
        public boolean cancelled() {
            return this.parent != null && this.parent.cancelled();
        }

        @Override
        public void cancel() {
            if (this.parent != null) {
                this.parent.cancel();
            }
        }

        @Override
        public boolean done() {
            return this.parent != null && this.parent.done();
        }

    }

}
