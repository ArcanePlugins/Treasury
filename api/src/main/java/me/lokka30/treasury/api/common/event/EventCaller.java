/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

class EventCaller<T> {

    private Set<EventSubscriber<T>> subscriptions = new TreeSet<>();
    private final ExecutorService eventCallThreads;

    EventCaller(Class<T> eventClass) {
        this.eventCallThreads = Executors.newCachedThreadPool(new ThreadFactory() {

            private AtomicInteger number = new AtomicInteger(0);

            @Override
            public Thread newThread(@NotNull final Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Event " + eventClass.getSimpleName() + " caller thread #" + number.get());
                number.incrementAndGet();
                return thread;
            }
        });
    }

    public void register(@NotNull EventSubscriber<T> subscriber) {
        subscriptions.add(Objects.requireNonNull(subscriber, "subscriber"));
    }

    public Completion call(T event) {
        if (subscriptions.isEmpty()) {
            return Completion.completed();
        }
        Completion completion = new Completion();
        eventCallThreads.submit(() -> {
            call(event, this.subscriptions);
            completion.complete();
        });
        return completion;
    }

    public void shutdown() {
        eventCallThreads.shutdown();
    }

    private void call(T event, Set<EventSubscriber<T>> subscribers) {
        Set<EventSubscriber<T>> copy = new TreeSet<>(subscribers);
        for (EventSubscriber<T> subscriber : subscribers) {
            copy.remove(subscriber);
            subscriber.onEvent(event).whenComplete(errors -> {
                if (errors != null) {
                    for (Throwable e : errors) {
                        new TreasuryEventException(
                                "Could not call " + event
                                        .getClass()
                                        .getSimpleName() + " due to an exception caused by an event subscriber",
                                e
                        ).printStackTrace();
                    }
                    return;
                }

                call(event, copy);
            });
        }
    }

}
