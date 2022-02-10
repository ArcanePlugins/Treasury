package me.lokka30.treasury.api.common.event;

import java.util.concurrent.CountDownLatch;

class LogAwaiter {

    private CountDownLatch latch;

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
        System.out.println(message);
        latch.countDown();
    }

}
