/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.api.economy.response;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * An interface accepting responses from an economic provider.
 * Used to subscribe to a request that will be completed at
 * some point in the future.
 *
 * @param <T> the type of value expected on success
 * @since v1.0.0
 */
@SuppressWarnings("unused")
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

    /**
     * Wrap a method accepting an {@link EconomySubscriber} in a {@link CompletableFuture}.
     *
     * <p>This allows easy conversion from the more expressive style used by Treasury
     * into a quicker to use format. For example, setting a player's balance:
     * <pre>
     *     public void setBalance(
     *             &#64;NotNull CommandSender issuer,
     *             &#64;NotNull UUID target,
     *             double balance,
     *             &#64;NotNull Currency currency) {
     *         EconomyProvider economy = // Obtain provider
     *
     *         // First we need to obtain the account.
     *         EconomySubscriber.asFuture(subscriber -> economy.retrievePlayerAccount(target, subscriber))
     *             // Then we set the balance.
     *             .thenCompose(account -> EconomySubscriber.asFuture(subscriber -> account.setBalance(balance, currency, subscriber)))
     *             // And then we can use the final value however we like.
     *             .whenComplete((newBalance, exception) -> {
     *                 if (exception != null) {
     *                     sender.sendMessage("Something went wrong!");
     *                 } else {
     *                     sender.sendMessage(String.format("Set balance to %s.", newBalance));
     *                 }
     *             });
     *     }
     * </pre>
     * Note that due to the lack of explict requirement it is far easier to
     * forget exception handling.
     *
     * @param subscriberConsumer a {@link Consumer} accepting an {@code EconomySubscriber}
     * @param <T> the type of value expected by the {@code EconomySubscriber}
     * @return a future awaiting subscriber completion
     */
    static <T> @NotNull CompletableFuture<T> asFuture(@NotNull Consumer<EconomySubscriber<T>> subscriberConsumer) {
        CompletableFuture<T> future = new CompletableFuture<>();
        EconomySubscriber<T> subscriber = new EconomySubscriber<T>() {
            @Override
            public void succeed(@NotNull T t) {
                future.complete(t);
            }

            @Override
            public void fail(@NotNull EconomyException exception) {
                future.completeExceptionally(exception);
            }
        };

        subscriberConsumer.accept(subscriber);

        return future;
    }

}
