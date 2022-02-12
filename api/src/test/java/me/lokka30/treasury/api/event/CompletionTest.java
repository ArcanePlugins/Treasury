/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.event;

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
                log.log("this should've been logged after 2 seconds");
                completion.complete();
            }, 2, TimeUnit.SECONDS);
            return completion;
        }).completeSubscription());

        bus.fire(new Event());

        try {
            // wait 3 seconds to make sure the task has finished.
            // whilst yes we can use the returned Completion on EventBus#fire and run the assertion,
            // thing's that this is not what we're testing here.
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assertions.assertEquals(1, log.logs.size());
        Assertions.assertEquals("this should've been logged after 2 seconds", log.logs.get(0));
    }

}
