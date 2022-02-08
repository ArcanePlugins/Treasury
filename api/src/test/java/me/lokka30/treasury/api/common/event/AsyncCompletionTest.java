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
        LogCatcher log = new LogCatcher();

        bus.subscribe(bus
                .subscriptionFor(Event.class)
                .whenCalled((Consumer<Event>) event -> log.log("whenCalled"))
                .completeSubscription());

        bus.fire(new Event()).whenCompleteAsync((event, errors) -> log.log("whenCompleteAsync"));

        log.log("direct");

        try {
            // sleep 1 second before checking just to make sure everything has passed
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assertions.assertEquals(3, log.logs.size());
        Assertions.assertEquals("direct", log.logs.get(0));
        Assertions.assertEquals("whenCalled", log.logs.get(1));
        Assertions.assertEquals("whenCompleteAsync", log.logs.get(2));
    }

}
