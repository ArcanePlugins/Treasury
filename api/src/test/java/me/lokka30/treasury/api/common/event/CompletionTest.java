/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CompletionTest {

    static class Event {

    }

    @Test
    void testCompletion() {
        EventBus bus = EventBus.INSTANCE;
        ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();
        LogCatcher log = new LogCatcher();

        bus.subscribe(bus.subscriptionFor(Event.class).whenCalled(event -> {
            Completion completion = new Completion();
            scheduled.schedule(() -> {
                log.log("this should've been logged after 200ms");
                completion.complete();
            }, 200, TimeUnit.MILLISECONDS);
            return completion;
        }).completeSubscription());

        bus.fire(new Event()).waitCompletion();

        Assertions.assertEquals(1, log.logs.size());
        Assertions.assertEquals("this should've been logged after 200ms", log.logs.get(0));
    }

}
