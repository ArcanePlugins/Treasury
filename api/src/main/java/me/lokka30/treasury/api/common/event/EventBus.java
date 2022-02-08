/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

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

public enum EventBus {
    INSTANCE;

    private Map<Class<?>, EventCaller> events = new ConcurrentHashMap<>();
    private EventTypeTracker eventTypes = new EventTypeTracker();

    public <T> void subscribe(@NotNull EventSubscriber<T> subscription) {
        Objects.requireNonNull(subscription, "subscription");
        events
                .computeIfAbsent(subscription.eventClass(),
                        k -> new EventCaller(subscription.eventClass())
                )
                .register(subscription);
    }

    @NotNull
    public <T> EventSubscriberBuilder<T> subscriptionFor(@NotNull Class<T> eventClass) {
        return new EventSubscriberBuilder<>(eventClass);
    }

    @NotNull
    public <T> Completion fire(@NotNull T event) {
        Objects.requireNonNull(event, "event");
        List<Class<?>> friends = eventTypes.getFriendsOf(event.getClass());
        ExecutorService async = EventExecutorTracker.INSTANCE.getExecutor(event.getClass());
        Completion ret = new Completion(event.getClass());
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
                Completion
                        .join(event.getClass(), completions.toArray(new Completion[0]))
                        .whenCompleteBlocking(errors -> {
                            if (!errors.isEmpty()) {
                                ret.completeExceptionally(errors);
                            } else {
                                ret.complete();
                            }
                        });
            } else {
                ret.complete();
            }
        });
        return ret;
    }

    public static final class EventSubscriberBuilder<T> {

        private final Class<T> eventClass;
        private EventPriority priority;
        private boolean ignoreCancelled = false;
        private Consumer<T> eventConsumer;
        private Function<T, Completion> completions;

        private EventSubscriberBuilder(@NotNull Class<T> eventClass) {
            this.eventClass = Objects.requireNonNull(eventClass, "eventClass");
        }

        @Contract("_ -> this")
        public EventSubscriberBuilder<T> withPriority(@NotNull EventPriority priority) {
            this.priority = Objects.requireNonNull(priority, "priority");
            return this;
        }

        @Contract("_ -> this")
        public EventSubscriberBuilder<T> ignoreCancelled(boolean ignoreCancelled) {
            this.ignoreCancelled = ignoreCancelled;
            return this;
        }

        @Contract("_ -> this")
        public EventSubscriberBuilder<T> whenCalled(@NotNull Consumer<T> eventConsumer) {
            this.eventConsumer = Objects.requireNonNull(eventConsumer, "eventConsumer");
            return this;
        }

        @Contract("_ -> this")
        public EventSubscriberBuilder<T> whenCalled(@NotNull Function<T, Completion> withCompletion) {
            this.completions = Objects.requireNonNull(withCompletion, "withCompletion");
            return this;
        }

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
