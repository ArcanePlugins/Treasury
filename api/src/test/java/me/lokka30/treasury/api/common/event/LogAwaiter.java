package me.lokka30.treasury.api.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class LogAwaiter {

    private CountDownLatch latch;
    List<String> logs = new ArrayList<>();

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

}
