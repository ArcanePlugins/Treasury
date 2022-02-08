/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
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
        LogCatcher log = new LogCatcher();
        bus.subscribe(bus.subscriptionFor(FirstEvent.class).whenCalled(event -> {
            log.log("FirstEvent");
            countdown.decrementAndGet();
        }).completeSubscription());

        bus.subscribe(bus.subscriptionFor(SecondEvent.class).whenCalled(event -> {
            log.log("SecondEvent");
            countdown.decrementAndGet();
        }).completeSubscription());

        bus.fire(new SecondEvent()).whenCompleteAsync(errors -> {
            log.log("Called SecondEvent");
            Assertions.assertEquals(0, countdown.get());
            Assertions.assertEquals(3, log.logs.size());
            Assertions.assertEquals("SecondEvent", log.logs.get(0));
            Assertions.assertEquals("FirstEvent", log.logs.get(1));
            Assertions.assertEquals("Called SecondEvent", log.logs.get(2));
        });
    }

}
