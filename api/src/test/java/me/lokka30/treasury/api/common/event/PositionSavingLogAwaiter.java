/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

class PositionSavingLogAwaiter {

    private CountDownLatch latch;
    Map<Integer, String> logs = new ConcurrentHashMap<>();

    PositionSavingLogAwaiter(int logCount) {
        latch = new CountDownLatch(logCount);
    }

    void startWaiting() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void log(String message) {
        logs.put(logs.size(), message);
        latch.countDown();
    }

}
