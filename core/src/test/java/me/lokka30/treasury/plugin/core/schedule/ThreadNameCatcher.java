/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

class ThreadNameCatcher {

    enum Identifier {
        MAIN,
        ASYNC
    }

    Map<Identifier, String> names = new HashMap<>();
    CountDownLatch asyncCaught = new CountDownLatch(1);

    void catchName(Identifier identifier) {
        this.names.put(identifier, Thread.currentThread().getName());
        if (identifier == Identifier.ASYNC) {
            asyncCaught.countDown();
        }
    }

    void whenAsyncCaught(Runnable run) {
        try {
            asyncCaught.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        run.run();
    }

}
