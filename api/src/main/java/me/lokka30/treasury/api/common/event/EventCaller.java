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

class EventCaller {

    private Set<EventSubscriber> subscriptions = new TreeSet<>();
    private final ExecutorService eventCallThreads;

    EventCaller(Class<?> eventClass) {
        this.eventCallThreads = Executors.newCachedThreadPool(new ThreadFactory() {

            private AtomicInteger amountOfThreads = new AtomicInteger(0);

            @Override
            public Thread newThread(@NotNull final Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Event " + eventClass.getSimpleName() + " caller thread #" + amountOfThreads.get());
                amountOfThreads.incrementAndGet();
                return thread;
            }
        });
    }

    public void register(@NotNull EventSubscriber subscriber) {
        subscriptions.add(Objects.requireNonNull(subscriber, "subscriber"));
    }

    public Completion call(Object event) {
        if (subscriptions.isEmpty()) {
            return Completion.completed();
        }
        Completion completion = new Completion(eventCallThreads);
        eventCallThreads.submit(() -> {
            call(event, this.subscriptions);
            completion.complete();
        });
        return completion;
    }

    public ExecutorService eventCallThreads() {
        return eventCallThreads;
    }

    public void shutdown() {
        eventCallThreads.shutdown();
    }

    private void call(Object event, Set<EventSubscriber> subscribers) {
        Set<EventSubscriber> copy = new TreeSet<>(subscribers);
        for (EventSubscriber subscriber : subscribers) {
            copy.remove(subscriber);
            if (event instanceof Cancellable) {
                if (((Cancellable) event).isCancelled() && subscriber.ignoreCancelled()) {
                    continue;
                }
            }
            subscriber.onEvent(event).whenCompleteBlocking(errors -> {
                if (!errors.isEmpty()) {
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
