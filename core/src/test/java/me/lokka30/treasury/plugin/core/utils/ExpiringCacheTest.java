package me.lokka30.treasury.plugin.core.utils;

import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

public class ExpiringCacheTest {

    private ExpiringCache<String> cache = new ExpiringCache<>(2, TimeUnit.SECONDS);

    @Test
    public void testExpiration() {
        cache.set("foo");
        System.out.println("Value after set: " + cache.get());
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertNull(cache.get());
    }
}
