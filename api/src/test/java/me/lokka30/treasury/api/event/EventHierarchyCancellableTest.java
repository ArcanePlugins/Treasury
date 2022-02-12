package me.lokka30.treasury.api.event;

import java.util.concurrent.atomic.AtomicInteger;
import me.lokka30.treasury.api.event.Cancellable;
import me.lokka30.treasury.api.event.EventBus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EventHierarchyCancellableTest {

    static class FirstEvent implements Cancellable {

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

    static class SecondEvent extends FirstEvent {

    }

    @Test
    void testCancellableCall() {
        EventBus bus = EventBus.INSTANCE;
        AtomicInteger latch = new AtomicInteger(3);

        bus.subscribe(bus.subscriptionFor(Cancellable.class).whenCalled(event -> {
            latch.decrementAndGet();
        }).completeSubscription());

        bus.subscribe(bus.subscriptionFor(FirstEvent.class).whenCalled(event -> {
            latch.decrementAndGet();
        }).completeSubscription());

        bus.subscribe(bus.subscriptionFor(SecondEvent.class).whenCalled(event -> {
            latch.decrementAndGet();
        }).completeSubscription());

        bus.fire(new SecondEvent()).whenCompleteBlocking((event, errors) -> {
            Assertions.assertEquals(1, latch.get());
        });
    }

}
