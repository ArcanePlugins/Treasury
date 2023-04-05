package me.lokka30.treasury.api.common.event;

import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AsyncCompletionTest {

    static class Event {

    }

    //@Test // TODO: inconsistent outputs. Was fixed once, but still inconsistent.
    void testAsyncCompletion() {
        EventBus bus = EventBus.INSTANCE;
        LogAwaiter log = new LogAwaiter(3);

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
