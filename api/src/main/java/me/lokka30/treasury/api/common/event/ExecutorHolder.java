/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

class ExecutorHolder {

    public static final ExecutorHolder INSTANCE = new ExecutorHolder();

    private ExecutorHolder() {

    }

    private ExecutorService executor;

    @NotNull
    public ExecutorService getExecutor() {
        if (this.executor == null) {
            this.executor = Executors.newCachedThreadPool(new ThreadFactory() {

                private AtomicInteger amountOfThreads = new AtomicInteger(0);

                @Override
                @NotNull
                public Thread newThread(@NotNull final Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("Event caller thread #" + amountOfThreads.get());
                    amountOfThreads.incrementAndGet();
                    return thread;
                }
            });
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (this.executor != null) {
                    this.executor.shutdown();
                }
            }));
        }
        return this.executor;
    }

}
