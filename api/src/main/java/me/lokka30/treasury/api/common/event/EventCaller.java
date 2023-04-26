/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import me.lokka30.treasury.api.common.misc.SortedList;
import org.jetbrains.annotations.NotNull;

class EventCaller {

    private List<EventSubscriber> subscriptions = new SortedList<>();

    EventCaller() {
    }

    public void register(@NotNull EventSubscriber subscriber) {
        subscriptions.add(Objects.requireNonNull(subscriber, "subscriber"));
    }

    @NotNull
    public Completion call(@NotNull Object event) {
        if (subscriptions.isEmpty()) {
            return Completion.completed();
        }
        Completion completion = new Completion();
        ExecutorHolder.INSTANCE.getExecutor().execute(() -> {
            ParallelProcessing parallelAnno = event
                    .getClass()
                    .getAnnotation(ParallelProcessing.class);
            if (parallelAnno == null) {
                List<Throwable> errors = call(event, new ArrayList<>(), 0);
                if (!errors.isEmpty()) {
                    completion.completeExceptionally(errors);
                } else {
                    completion.complete();
                }
            } else {
                parallelCall(event, (errors) -> {
                    if (!errors.isEmpty()) {
                        completion.completeExceptionally(errors);
                    } else {
                        completion.complete();
                    }
                });
            }
        });
        return completion;
    }

    private List<Throwable> call(Object event, List<Throwable> errorsToThrow, int startIndex) {
        EventSubscriber subscriber = subscriptions.get(startIndex);
        final int nextStart = startIndex + 1;
        subscriber.onEvent(event).whenComplete(errors -> {
            if (!errors.isEmpty()) {
                errorsToThrow.addAll(errors);
                return;
            }

            if (nextStart < subscriptions.size()) {
                call(event, errorsToThrow, nextStart);
            }
        });

        return errorsToThrow;
    }

    private void parallelCall(Object event, Consumer<Set<Throwable>> callback) {
        CountDownLatch latch = new CountDownLatch(subscriptions.size());
        Set<Throwable> errors = ConcurrentHashMap.newKeySet();
        for (EventSubscriber subscriber : subscriptions) {
            ExecutorHolder.INSTANCE.getExecutor().submit(() -> {
                subscriber.onEvent(event).whenComplete(e -> {
                    if (!e.isEmpty()) {
                        errors.addAll(e);
                    }
                    latch.countDown();
                });
            });
        }
        try {
            latch.await();
            callback.accept(errors);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
