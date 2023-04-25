package me.lokka30.treasury.api.common.event;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

class LogAwaiter {

    private CountDownLatch latch;
    Set<String> logs = ConcurrentHashMap.newKeySet();

    LogAwaiter(int logCount) {
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
        logs.add(message);
        latch.countDown();
    }

    String getLog(int pos) {
        int idx = 0;
        for (String log : logs) {
            if (pos == idx) {
                return log;
            }
            idx++;
        }
        return null;
    }

}
