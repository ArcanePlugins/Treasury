package me.lokka30.treasury.api.common.event;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IgnoreCancelledTest {

    static class Event implements Cancellable {

        private boolean cancelled;

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(final boolean cancel) {
            this.cancelled = cancel;
        }

    }

    @Test
    public void testIgnoreCancelled() {
        EventBus bus = EventBus.INSTANCE;
        AtomicInteger latch = new AtomicInteger(3);

        bus.subscribe(bus
                .subscriptionFor(Event.class)
                .withPriority(EventPriority.LOW)
                .whenCalled(event -> {
                    latch.decrementAndGet();
                    event.setCancelled(true);
                })
                .completeSubscription());

        bus.subscribe(bus.subscriptionFor(Event.class).ignoreCancelled(true).whenCalled(event -> {
            latch.decrementAndGet();
        }).completeSubscription());

        bus.subscribe(bus
                .subscriptionFor(Event.class)
                .withPriority(EventPriority.HIGH)
                .whenCalled(event -> {
                    latch.decrementAndGet();
                })
                .completeSubscription());

        bus.fire(new Event()).whenCompleteAsync(error -> {
            Assertions.assertEquals(1, latch.get());
        });
    }

}
