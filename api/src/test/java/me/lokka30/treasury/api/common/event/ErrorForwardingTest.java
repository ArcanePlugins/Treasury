/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ErrorForwardingTest {

    static class Event {

    }

    @Test
    void testErrorForwarding() {
        EventBus bus = EventBus.INSTANCE;

        bus.subscribe(bus
                .subscriptionFor(Event.class)
                .withPriority(EventPriority.LOW)
                .whenCalled((Consumer<Event>) event -> {
                    throw new RuntimeException();
                })
                .completeSubscription());

        AtomicInteger latch = new AtomicInteger(1);
        bus.subscribe(bus
                .subscriptionFor(Event.class)
                .withPriority(EventPriority.HIGH)
                .whenCalled((event) -> {
                    latch.decrementAndGet();
                })
                .completeSubscription());

        bus.fire(new Event()).whenCompleteBlocking((event, errors) -> {
            Assertions.assertFalse(errors.isEmpty());
            Assertions.assertEquals(1, errors.size());
            Assertions.assertTrue(errors
                    .stream()
                    .findFirst()
                    .get()
                    .getClass()
                    .isAssignableFrom(RuntimeException.class));
            Assertions.assertEquals(1, latch.get());
        });
    }


}
