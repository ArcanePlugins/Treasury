/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IgnoredCallOutcomeTest {

    static class TestEvent {

    }

    static final EventBus eventBus = EventBus.INSTANCE;
    static LogAwaiter logCatcher = new LogAwaiter(2);

    @BeforeAll
    static void prepare() {
        eventBus.subscribe(eventBus.subscriptionFor(TestEvent.class).whenCalled(foo -> {
            logCatcher.log("called");
        }).completeSubscription());
    }

    @Test
    void testIgnoredCallOutcome() {
        eventBus.fire(new TestEvent());
        logCatcher.log("fired");

        logCatcher.startWaiting();

        Assertions.assertEquals("fired", logCatcher.getLog(0));
    }

}
