package me.lokka30.treasury.api.economy.response;

import org.jetbrains.annotations.NotNull;

/**
 * An interface accepting responses from an economic provider.
 * Used to subscribe to a request that will be completed at
 * some point in the future.
 *
 * @param <T> the type of value expected on success
 * @since v1.0.0
 */
public interface EconomySubscriber<T> {

    /**
     * Respond to the subscriber with a successful invocation.
     *
     * @param t the value of the successful invocation
     * @since v1.0.0
     */
    void succeed(@NotNull T t);

    /**
     * Respond to the subscriber with an invocation failure.
     *
     * @param exception an {@link EconomyException} detailing the reason for failure
     * @since v1.0.0
     */
    void fail(@NotNull EconomyException exception);

}
