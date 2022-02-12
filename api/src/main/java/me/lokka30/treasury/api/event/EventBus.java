/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event bus. An event bus manages event subscriptions and event calls.
 *
 * @author MrIvanPlays
 * @since v1.1.0
 */
public enum EventBus {
    INSTANCE;

    private Map<Class<?>, EventCaller> events = new ConcurrentHashMap<>();
    private EventTypeTracker eventTypes = new EventTypeTracker();

    /**
     * Subscribes this {@link EventSubscriber} for calling whenever the event the specified
     * {@code subscription} is listening for gets fired.
     *
     * @param subscription the subscription to subscribe
     * @param <T> event type
     * @see EventSubscriber
     */
    public <T> void subscribe(@NotNull EventSubscriber<T> subscription) {
        Objects.requireNonNull(subscription, "subscription");
        events
                .computeIfAbsent(subscription.eventClass(),
                        k -> new EventCaller(subscription.eventClass())
                )
                .register(subscription);
    }

    /**
     * Creates a {@link EventSubscriberBuilder} for the specified {@code eventClass}
     *
     * @param eventClass the event class to create a subscriber builder
     * @param <T> event type
     * @return new event subscriber builder
     */
    @NotNull
    public <T> EventSubscriberBuilder<T> subscriptionFor(@NotNull Class<T> eventClass) {
        return new EventSubscriberBuilder<>(eventClass);
    }

    /**
     * Calls/Fires the {@link EventSubscriber EventSubscribers} of the specified {@code event}
     *
     * @param event the event to fire
     * @param <T> event type
     * @return {@link FireCompletion}
     * @see FireCompletion
     */
    @NotNull
    public <T> FireCompletion<T> fire(@NotNull T event) {
        Objects.requireNonNull(event, "event");
        List<Class<?>> friends = eventTypes.getFriendsOf(event.getClass());
        ExecutorService async = EventExecutorTracker.INSTANCE.getExecutor(event.getClass());
        FireCompletion<T> ret = new FireCompletion<>(event.getClass());
        async.submit(() -> {
            List<Completion> completions = new ArrayList<>();
            EventCaller caller = events.get(event.getClass());
            if (caller != null) {
                completions.add(caller.call(event));
            }
            for (Class<?> friend : friends) {
                EventCaller friendCaller = events.get(friend);
                if (friendCaller != null) {
                    completions.add(friendCaller.call(event));
                }
            }
            if (!completions.isEmpty()) {
                Completion.join(completions.toArray(new Completion[0])).whenComplete(errors -> {
                    if (!errors.isEmpty()) {
                        ret.completeExceptionally(errors);
                    } else {
                        ret.complete(event);
                    }
                });
            } else {
                ret.complete(event);
            }
        });
        return ret;
    }

    /**
     * Represents a builder of a {@link EventSubscriber}
     *
     * @param <T> event type
     * @author MrIvanPlays
     * @since v1.1.0
     */
    public static final class EventSubscriberBuilder<T> {

        private final Class<T> eventClass;
        private EventPriority priority;
        private boolean ignoreCancelled = false;
        private Consumer<T> eventConsumer;
        private Function<T, Completion> completions;

        private EventSubscriberBuilder(@NotNull Class<T> eventClass) {
            this.eventClass = Objects.requireNonNull(eventClass, "eventClass");
        }

        /**
         * Specifies the {@link EventPriority} of the currently building {@link EventSubscriber}
         *
         * @param priority priority
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public EventSubscriberBuilder<T> withPriority(@NotNull EventPriority priority) {
            this.priority = Objects.requireNonNull(priority, "priority");
            return this;
        }

        /**
         * Specifies whether the currently building {@link EventSubscriber} shall accept already
         * cancelled events.
         *
         * @param ignoreCancelled should ignore cancelled or not
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public EventSubscriberBuilder<T> ignoreCancelled(boolean ignoreCancelled) {
            this.ignoreCancelled = ignoreCancelled;
            return this;
        }

        /**
         * Specifies the action to do whenever the event the currently building
         * {@link EventSubscriber} subscribes for gets fired.
         *
         * @param eventConsumer the action to do
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public EventSubscriberBuilder<T> whenCalled(@NotNull Consumer<T> eventConsumer) {
            this.eventConsumer = Objects.requireNonNull(eventConsumer, "eventConsumer");
            return this;
        }

        /**
         * Specifies the action to do whenever the event the currently building
         * {@link EventSubscriber} subscribes for gets fired with the ability to block event
         * execution until an asynchronous task finishes.
         *
         * @param withCompletion the action to do
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public EventSubscriberBuilder<T> whenCalled(@NotNull Function<T, Completion> withCompletion) {
            this.completions = Objects.requireNonNull(withCompletion, "withCompletion");
            return this;
        }

        /**
         * Builds the specified parameters in this builder into a {@link EventSubscriber}
         *
         * @return event subscriber
         */
        @NotNull
        public EventSubscriber<T> completeSubscription() {
            if (priority == null) {
                priority = EventPriority.NORMAL;
            }
            if (eventConsumer != null) {
                return new SimpleEventSubscriber<T>(eventClass, priority, ignoreCancelled) {
                    @Override
                    public void subscribe(@NotNull final T event) {
                        eventConsumer.accept(event);
                    }
                };
            } else {
                Objects.requireNonNull(completions, "completions");
                return new EventSubscriber<T>(eventClass, priority, ignoreCancelled) {
                    @Override
                    @NotNull
                    public Completion onEvent(@NotNull final T event) {
                        return completions.apply(event);
                    }
                };
            }
        }

    }

}
