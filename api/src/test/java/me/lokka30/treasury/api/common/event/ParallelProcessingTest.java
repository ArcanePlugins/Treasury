/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.time.LocalTime;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ParallelProcessingTest {

    @ParallelProcessing
    static class ParallelEvent {

    }

    private static final EventBus bus = EventBus.INSTANCE;
    private static final LogCatcherAdvanced logCatcher = new LogCatcherAdvanced();

    @BeforeAll
    static void prepare() {
        for (int i = 0; i < 3; i++) {
            final int j = i;
            bus.subscribe(bus.subscriptionFor(ParallelEvent.class).whenCalled(event -> {
                logCatcher.log("Hello from subscription #" + j);
            }).completeSubscription());
        }
    }

    @Test
    void testCall() {
        bus.fire(new ParallelEvent()).waitCompletion();

        Set<Map.Entry<LocalTime, String>> entries = logCatcher.logs.entrySet();

        for (Map.Entry<LocalTime, String> entry : entries) {
            System.out.println(entry.getKey().toString() + " : " + entry.getValue());
        }
        String comparison = entries.stream().findFirst().orElse(null).getKey().toString().split(
                "\\.")[0];
        for (Map.Entry<LocalTime, String> entry : entries) {
            String current = entry.getKey().toString().split("\\.")[0];
            if (!comparison.equalsIgnoreCase(current)) {
                Assertions.fail();
            }
        }
    }

}
