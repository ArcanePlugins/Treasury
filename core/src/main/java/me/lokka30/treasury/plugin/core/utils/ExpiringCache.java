package me.lokka30.treasury.plugin.core.utils;

import java.util.concurrent.TimeUnit;

/**
 * Represents a value cache, which expires after a certain time period.
 *
 * @param <T> value type
 * @author MrIvanPlays
 * @since v1.0.0
 */
public class ExpiringCache<T> {

    private T value;
    private final long expiresAfterMillis;
    private long stampSetAt = -1;

    public ExpiringCache(int expiresAfter, TimeUnit expiresAfterUnit) {
        expiresAfterMillis = expiresAfterUnit.toMillis(expiresAfter);
    }

    /**
     * Returns a value if the cache has not expired, null otherwise.
     *
     * @return value or null
     */
    public T get() {
        if (value == null) {
            return null;
        }
        if (stampSetAt != -1) {
            long diff = System.currentTimeMillis() - stampSetAt;
            if (diff >= expiresAfterMillis && value != null) {
                value = null;
            }
        }
        return value;
    }

    /**
     * Sets a new value for this cache.
     *
     * @param value new value to be cached
     */
    public void set(T value) {
        this.value = value;
        this.stampSetAt = System.currentTimeMillis();
    }

}
