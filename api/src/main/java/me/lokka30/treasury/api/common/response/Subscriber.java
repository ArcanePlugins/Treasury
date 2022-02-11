package me.lokka30.treasury.api.common.response;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/**
 * An interface accepting responses from a provider.
 * Used to subscribe to a request that will be completed at
 * some point in the future.
 * Example usage see class level
 * javadoc of {@link me.lokka30.treasury.api.economy.response.EconomySubscriber}
 *
 * @param <T> the type of value expected on success
 * @author Jikoo
 * @since v1.1.0
 */
public interface Subscriber<T, E extends TreasuryException> {

    /**
     * Respond to the subscriber with a successful invocation.
     *
     * @param t the value of the successful invocation
     * @author Jikoo
     * @since v1.1.0
     */
    void succeed(@NotNull T t);

    /**
     * Respond to the subscriber with an invocation failure.
     *
     * @param exception an {@link E} detailing the reason for failure
     * @author Jikoo
     * @since v1.1.0
     */
    void fail(@NotNull E exception);

    /**
     * Wrap a method accepting an {@link Subscriber} in a {@link CompletableFuture}.
     * This allows easy conversion from the more expressive style used by Treasury
     * into a quicker to use format. For example, setting a player's balance:
     * <pre>
     * public void setBalance(
     *     &#64;NotNull CommandSender issuer,
     *     &#64;NotNull UUID target,
     *     &#64;NotNull BigDecimal balance,
     *     &#64;NotNull Currency currency
     * ) {
     *     final EconomyProvider economy = // Obtain provider
     *
     *     // Create initiator object: check if CommandSender is a player or not
     *     final EconomyTransactionInitiator<?> initiator;
     *     if(sender instanceof Player) {
     *         initiator = new EconomyTransactionInitiator<>() {
     *             &#64;Override
     *             public Object getData() { return ((Player) sender).getUniqueId(); }
     *
     *             &#64;Override
     *             public @NotNull Type getType() { return Type.PLAYER;}
     *         };
     *     } else {
     *         initiator = EconomyTransactionInitiator.SERVER;
     *     }
     *
     *     // Then we need to obtain the account.
     *     Subscriber.asFuture(subscriber -> economy.retrievePlayerAccount(target, subscriber))
     *
     *     // Then we set the balance.
     *     .thenCompose(account -> Subscriber.asFuture(subscriber -> account.setBalance(balance, initiator, currency, subscriber)))
     *
     *     // And then we can use the final value however we like.
     *     .whenComplete((newBalance, exception) -> {
     *         if (exception != null) {
     *             ender.sendMessage("Something went wrong!");
     *         } else {
     *             sender.sendMessage(String.format("Set balance to %s.", newBalance));
     *         }
     *     });
     * }
     * </pre>
     * Note that due to the lack of explicit requirement it is far easier to
     * forget exception handling.
     *
     * @param subscriberConsumer a {@link Consumer} accepting an {@code Subscriber}
     * @param <T>                the type of value expected by the {@code Subscriber}
     * @return a future awaiting subscriber completion
     * @author Jikoo
     * @since v1.1.0
     */
    static <T, E extends TreasuryException> @NotNull CompletableFuture<T> asFuture(@NotNull Consumer<?
            super Subscriber<T,E>> subscriberConsumer) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Subscriber<T, E> subscriber = new Subscriber<T, E>() {
            @Override
            public void succeed(@NotNull T t) {
                future.complete(t);
            }

            @Override
            public void fail(@NotNull E exception) {
                future.completeExceptionally(exception);
            }
        };

        subscriberConsumer.accept(subscriber);

        return future;
    }
}
