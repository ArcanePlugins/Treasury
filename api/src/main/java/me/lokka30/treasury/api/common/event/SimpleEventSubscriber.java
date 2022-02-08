/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import org.jetbrains.annotations.NotNull;

public abstract class SimpleEventSubscriber<T> extends EventSubscriber<T> {

    public SimpleEventSubscriber(@NotNull Class<T> eventClass) {
        super(eventClass);
    }

    public SimpleEventSubscriber(@NotNull Class<T> eventClass, boolean ignoreCancelled) {
        super(eventClass, ignoreCancelled);
    }

    public SimpleEventSubscriber(
            @NotNull Class<T> eventClass, @NotNull EventPriority priority
    ) {
        super(eventClass, priority);
    }

    public SimpleEventSubscriber(
            @NotNull Class<T> eventClass, @NotNull EventPriority priority, boolean ignoreCancelled
    ) {
        super(eventClass, priority, ignoreCancelled);
    }

    public abstract void subscribe(@NotNull T event);

    @Override
    @NotNull
    public Completion onEvent(@NotNull T event) {
        try {
            subscribe(event);
            return Completion.completed(eventClass());
        } catch (Throwable error) {
            return Completion.completedExceptionally(error, eventClass());
        }
    }

    @Override
    public String toString() {
        return "Simple" + super.toString();
    }

}
