/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a subscriber of an event. This should be used only if you want to block event
 * execution.
 *
 * <p>Examples:
 * <pre>
 * EventBus eventBus = EventBus.INSTANCE;
 * eventBus.subscribe(
 *   eventBus.subscriptionFor(AnEvent.class)
 *     .withPriority(EventPriority.HIGH)
 *     .whenCalled(event -> {
 *       Completion completion = new Completion();
 *       doAsync(() -> {
 *         // do something
 *         // then you run either completion.complete() or completion.completeWithException(error)
 *         // if you don't call it the event won't proceed, potentially blocking the transaction so please don't forget to do this before doing a release or sth of your plugin
 *         completion.complete();
 *       });
 *       return completion;
 *     });
 *     .completeSubscription()
 * );
 *
 * public class MyEventListener implements EventSubscriber&#60;MyEvent&#62; {
 *
 *   public MyEventListener() {
 *     super(MyEvent.class, EventPriority.NORMAL);
 *   }
 *
 *   &#64;Override
 *   public Completion onEvent(MyEvent event) {
 *     Completion completion = new Completion();
 *     doAsync(() -> {
 *       // do something
 *       // then you run either completion.complete() or completion.completeWithException(error)
 *       // if you don't call it the event won't proceed, potentially blocking the transaction so please don't forget to do this before doing a release or sth of your plugin
 *       completion.complete();
 *     });
 *     return completion;
 *   }
 * }
 *
 * // then you register
 * EventBus.INSTANCE.subscribe(new MyEventListener());
 * </pre>
 *
 * @param <T> event type
 * @author MrIvanPlays
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
 */
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

    /**
     * Returns the event class this subscriber has a subscription to.
     *
     * @return event class
     */
    @NotNull
    public Class<T> eventClass() {
        return eventClass;
    }

    /**
     * Returns the {@link EventPriority} of this subscriber.
     *
     * @return priority
     */
    @NotNull
    public EventPriority priority() {
        return priority;
    }

    /**
     * Returns whether this subscriber is ignoring already cancelled events or not.
     *
     * @return true if ignore cancelled
     */
    public boolean ignoreCancelled() {
        return ignoreCancelled;
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

    /**
     * Treasury's {@link EventBus} calls this method whenever a {@link EventBus#fire(Object)}
     * occurs with the event this subscription listens for.
     *
     * @param event the event
     * @return completion
     * @see Completion
     */
    @NotNull
    public abstract Completion onEvent(@NotNull T event);

}
