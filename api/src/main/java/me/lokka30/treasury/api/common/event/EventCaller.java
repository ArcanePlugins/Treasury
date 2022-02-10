/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.lokka30.treasury.api.common.misc.SortedList;
import org.jetbrains.annotations.NotNull;

class EventCaller {

    private List<EventSubscriber> subscriptions = new SortedList<>();
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
            List<Throwable> errors = call(event, new ArrayList<>(), 0);
            if (!errors.isEmpty()) {
                completion.completeExceptionally(errors);
            } else {
                completion.complete();
            }
        });
        return completion;
    }

    private List<Throwable> call(Object event, List<Throwable> errorsToThrow, int startIndex) {
        for (int i = startIndex; i < subscriptions.size(); i++) {
            EventSubscriber subscriber = subscriptions.get(i);
            if (event instanceof Cancellable) {
                if (((Cancellable) event).isCancelled() && subscriber.ignoreCancelled()) {
                    continue;
                }
            }
            final int nextStart = i + 1;
            subscriber.onEvent(event).whenComplete(errors -> {
                if (!errors.isEmpty()) {
                    errorsToThrow.addAll(errors);
                    return;
                }

                call(event, errorsToThrow, nextStart);
            });
            break;
        }
        return errorsToThrow;
    }

}
