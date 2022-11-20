/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

class EventExecutorTracker {

    public static final EventExecutorTracker INSTANCE = new EventExecutorTracker();

    private EventExecutorTracker() {

    }

    private Map<Class<?>, ExecutorService> executors = new ConcurrentHashMap<>();

    @NotNull
    public ExecutorService getExecutor(@NotNull Class<?> eventClass) {
        return executors.computeIfAbsent(eventClass,
                k -> Executors.newCachedThreadPool(new ThreadFactory() {

                    private AtomicInteger amountOfThreads = new AtomicInteger(0);

                    @Override
                    @NotNull
                    public Thread newThread(@NotNull final Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("Event " + eventClass.getSimpleName() + " caller thread #" + amountOfThreads.get());
                        amountOfThreads.incrementAndGet();
                        return thread;
                    }
                })
        );
    }

    void shutdown() {
        for (ExecutorService executor : executors.values()) {
            executor.shutdown();
        }
    }

}
