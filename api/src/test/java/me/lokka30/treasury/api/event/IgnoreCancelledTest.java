/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.event;

import java.util.concurrent.atomic.AtomicInteger;
import me.lokka30.treasury.api.event.Cancellable;
import me.lokka30.treasury.api.event.EventBus;
import me.lokka30.treasury.api.event.EventPriority;
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

        bus.fire(new Event()).whenCompleteBlocking((event, errors) -> Assertions.assertEquals(1,
                latch.get()
        ));
    }

}
