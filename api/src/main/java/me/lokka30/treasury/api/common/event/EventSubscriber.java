/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public abstract class EventSubscriber<T> implements Comparable<EventSubscriber<T>> {

    private final Class<T> eventClass;
    private final EventPriority priority;
    private final boolean ignoreCancelled;

    public EventSubscriber(@NotNull Class<T> eventClass) {
        this(eventClass, EventPriority.NORMAL);
    }

    public EventSubscriber(@NotNull Class<T> eventClass, boolean ignoreCancelled) {
        this(eventClass, EventPriority.NORMAL, ignoreCancelled);
    }

    public EventSubscriber(@NotNull Class<T> eventClass, @NotNull EventPriority priority) {
        this(eventClass, priority, false);
    }

    public EventSubscriber(
            @NotNull Class<T> eventClass, @NotNull EventPriority priority, boolean ignoreCancelled
    ) {
        this.eventClass = Objects.requireNonNull(eventClass, "eventClass");
        this.priority = Objects.requireNonNull(priority, "priority");
        this.ignoreCancelled = ignoreCancelled;
    }

    @NotNull
    public Class<T> eventClass() {
        return eventClass;
    }

    @NotNull
    public EventPriority priority() {
        return priority;
    }

    public boolean ignoreCancelled() {
        return ignoreCancelled;
    }

    @NotNull
    public Completion createCompletion() {
        return EventBus.INSTANCE.createCompletion(eventClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(EventSubscriber<T> other) {
        return priority.compareTo(other.priority());
    }

    @Override
    public String toString() {
        return "EventSubscriber{eventClass=" + eventClass + ", priority=" + priority + '}';
    }

    @NotNull
    public abstract Completion onEvent(@NotNull T event);

}
