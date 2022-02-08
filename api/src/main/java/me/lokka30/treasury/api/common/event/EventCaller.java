/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;

class EventCaller {

    private Set<EventSubscriber> subscriptions = new TreeSet<>();
    private final Class<?> eventClass;

    EventCaller(Class<?> eventClass) {
        this.eventClass = eventClass;
    }

    public void register(@NotNull EventSubscriber subscriber) {
        subscriptions.add(Objects.requireNonNull(subscriber, "subscriber"));
    }

    public Completion call(Object event) {
        if (subscriptions.isEmpty()) {
            return Completion.completed();
        }
        Completion completion = new Completion();
        EventExecutorTracker.INSTANCE.getExecutor(eventClass).submit(() -> {
            List<Throwable> errors = call(event, this.subscriptions, new ArrayList<>());
            if (!errors.isEmpty()) {
                completion.completeExceptionally(errors);
            } else {
                completion.complete();
            }
        });
        return completion;
    }

    private List<Throwable> call(
            Object event, Set<EventSubscriber> subscribers, List<Throwable> errorsToThrow
    ) {
        Set<EventSubscriber> copy = new TreeSet<>(subscribers);
        for (EventSubscriber subscriber : subscribers) {
            copy.remove(subscriber);
            if (event instanceof Cancellable) {
                if (((Cancellable) event).isCancelled() && subscriber.ignoreCancelled()) {
                    continue;
                }
            }
            subscriber.onEvent(event).whenComplete(errors -> {
                if (!errors.isEmpty()) {
                    errorsToThrow.addAll(errors);
                    return;
                }

                call(event, copy, errorsToThrow);
            });
            break;
        }
        return errorsToThrow;
    }

}
