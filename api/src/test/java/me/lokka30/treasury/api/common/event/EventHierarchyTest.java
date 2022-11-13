/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EventHierarchyTest {

    static class FirstEvent {

    }

    static class SecondEvent extends FirstEvent {

    }

    @Test
    void testEventHierarchy() {
        EventBus bus = EventBus.INSTANCE;
        AtomicInteger countdown = new AtomicInteger(2);
        bus.subscribe(bus.subscriptionFor(FirstEvent.class).whenCalled(event -> {
            countdown.decrementAndGet();
        }).completeSubscription());

        bus.subscribe(bus.subscriptionFor(SecondEvent.class).whenCalled(event -> {
            countdown.decrementAndGet();
        }).completeSubscription());

        bus.fire(new SecondEvent()).whenCompleteBlocking((event, errors) -> Assertions.assertEquals(0,
                countdown.get()
        ));
    }

}
