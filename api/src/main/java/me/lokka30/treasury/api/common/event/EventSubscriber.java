/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.Objects;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a subscriber of an event. An event subscriber is an object that listens for incoming
 * event calls.
 * <br>
 * Best practice is to use this for complex event subscribers, e.g. when event call block is needed,
 * and use the {@link SimpleEventSubscriber} for so-called "simple" event subscribers.
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
 * public class MyEventListener extends EventSubscriber&#60;MyEvent&#62; {
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
 * @since v1.1.0
 */
public abstract class EventSubscriber<T> implements Comparable<EventSubscriber<T>> {

    /**
     * Creates a new {@link EventSubscriber} via a {@link Function}
     *
     * @param eventClass  event class
     * @param priority    event priority
     * @param onEventFunc on event handler function
     * @param <T>         event type
     * @return event subscriber
     */
    @NotNull
    public static <T> EventSubscriber<T> functional(
            @NotNull Class<T> eventClass,
            @NotNull EventPriority priority,
            @NotNull Function<T, Completion> onEventFunc
    ) {
        return new EventSubscriber<T>(eventClass, priority) {
            @Override
            public @NotNull Completion onEvent(@NotNull T event) {
                return onEventFunc.apply(event);
            }
        };
    }

    private final Class<T> eventClass;
    private final EventPriority priority;

    public EventSubscriber(@NotNull Class<T> eventClass) {
        this(eventClass, EventPriority.NORMAL);
    }

    public EventSubscriber(
            @NotNull Class<T> eventClass, @NotNull EventPriority priority
    ) {
        this.eventClass = Objects.requireNonNull(eventClass, "eventClass");
        this.priority = Objects.requireNonNull(priority, "priority");
    }

    /**
     * A utility method for registering this event subscriber into the {@link EventBus event bus}.
     */
    public void register() {
        EventBus.INSTANCE.subscribe(this);
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
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull EventSubscriber<T> other) {
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
