/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AsyncCompletionTest {

    static class Event {

    }

    @Test
    void testAsyncCompletion() {
        EventBus bus = EventBus.INSTANCE;
        PositionSavingLogAwaiter log = new PositionSavingLogAwaiter(3);

        log.log("direct");

        bus.subscribe(bus
                .subscriptionFor(Event.class)
                .whenCalled((Consumer<Event>) event -> log.log("whenCalled"))
                .completeSubscription());

        bus.fire(new Event()).whenCompleteAsync((event, errors) -> log.log("whenCompleteAsync"));

        log.startWaiting();

        Assertions.assertEquals(3, log.logs.size());
        Assertions.assertEquals("direct", log.logs.get(0));
        Assertions.assertEquals("whenCalled", log.logs.get(1));
        Assertions.assertEquals("whenCompleteAsync", log.logs.get(2));
    }

}
