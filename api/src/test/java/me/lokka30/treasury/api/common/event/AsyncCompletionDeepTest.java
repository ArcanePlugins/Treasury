/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AsyncCompletionDeepTest {

    static class Event {

    }

    private static AtomicInteger latch = new AtomicInteger(100);

    @BeforeAll
    static void initialize() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                registerCompletionListener(executor);
            } else {
                registerNormalListener();
            }
        }
    }

    @Test
    void testAsyncCompletionDeep() {
        LogAwaiter logAwaiter = new LogAwaiter(1);

        EventBus.INSTANCE.fire(new Event())
                .whenCompleteAsync((event, errors) -> {
                    if (!errors.isEmpty()) {
                        for (Throwable e : errors) {
                            e.printStackTrace();
                        }
                        Assertions.fail(errors.stream().findFirst().get());
                        return;
                    }

                    logAwaiter.log(String.valueOf(latch.get()));
                });

        logAwaiter.startWaiting();
        Assertions.assertEquals(0, latch.get());
    }

    static void registerNormalListener() {
        EventBus bus = EventBus.INSTANCE;
        bus.subscribe(bus.subscriptionFor(Event.class).whenCalled(event -> {
            latch.decrementAndGet();
        }).completeSubscription());
    }

    static void registerCompletionListener(ScheduledExecutorService executor) {
        EventBus bus = EventBus.INSTANCE;
        bus.subscribe(bus.subscriptionFor(Event.class).whenCalled(event -> {
            Completion completion = new Completion();
            executor.schedule(() -> {
                latch.decrementAndGet();
                completion.complete();
            }, 50, TimeUnit.MILLISECONDS);
            return completion;
        }).completeSubscription());
    }

}
