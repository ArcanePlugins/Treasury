/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EventPriorityTest {

    static class Event {

    }

    @Test
    void testEventPriority() {
        EventBus bus = EventBus.INSTANCE;
        LogCatcher log = new LogCatcher();
        bus.subscribe(bus
                .subscriptionFor(Event.class)
                .withPriority(EventPriority.HIGH)
                .whenCalled(event -> {
                    log.log("HIGH");
                })
                .completeSubscription());

        bus.subscribe(bus
                .subscriptionFor(Event.class)
                .withPriority(EventPriority.LOW)
                .whenCalled(event -> {
                    log.log("LOW");
                })
                .completeSubscription());

        bus.fire(new Event()).whenCompleteBlocking((event, errors) -> {
            Assertions.assertEquals("LOW", log.logs.get(0));
            Assertions.assertEquals("HIGH", log.logs.get(1));
        });
    }

}
