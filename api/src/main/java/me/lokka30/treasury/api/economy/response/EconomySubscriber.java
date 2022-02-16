/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.response;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import me.lokka30.treasury.api.common.response.Subscriber;
import me.lokka30.treasury.api.common.response.TreasuryException;
import org.jetbrains.annotations.NotNull;

/**
 * An interface accepting responses from an economic provider.
 * Used to subscribe to a request that will be completed at
 * some point in the future.
 * Example usage:
 * <pre>
 * public void setBalance(
 *     &#64;NotNull CommandSender sender,
 *     &#64;NotNull UUID target,
 *     &#64;NotNull BigDecimal balance,
 *     &#64;NotNull Currency currency
 * ) {
 *     final EconomyProvider economy = //Obtain provider
 *
 *     // Create initiator object: check if CommandSender is a player or not
 *     final EconomyTransactionInitiator&#60;?&#62; initiator;
 *     if(sender instanceof Player) {
 *         initiator = new EconomyTransactionInitiator&#60;&#62;() {
 *             &#64;Override
 *             public Object getData() { return ((Player) sender).getUniqueId(); }
 *
 *             &#64;Override
 *             public &#64;NotNull Type getType() { return Type.PLAYER;}
 *         };
 *     } else {
 *         initiator = EconomyTransactionInitiator.SERVER;
 *     }
 *
 *     economy.retrievePlayerAccount(target, new EconomySubscriber&#60;&#62;() {
 *         &#64;Override
 *         public void succeed(@NotNull PlayerAccount account) {
 *             account.setBalance(balance, initiator, currency, new EconomySubscriber&#60;&#62;() {
 *                 &#64;Override
 *                 public void succeed(@NotNull BigDecimal newBalance) {
 *                     sender.sendMessage(String.format("Set balance to %s.", newBalance));
 *                 }
 *
 *                 &#64;Override
 *                 public void fail(@NotNull EconomyException exception) {
 *                     sender.sendMessage("Something went wrong!");
 *                 }
 *             });
 *         }
 *
 *         &#64;Override
 *         public void fail(@NotNull EconomyException exception) {
 *             sender.sendMessage("Something went wrong!");
 *         }
 *     });
 * }
 * </pre>
 *
 * @param <T> the type of value expected on success
 * @author Jikoo
 * @since v1.0.0
 */
public interface EconomySubscriber<T> extends Subscriber<T, EconomyException> {

    /**
     * Wrap a method accepting an {@link EconomySubscriber} in a {@link CompletableFuture}.
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
     *     final EconomyTransactionInitiator&#60;?&#62; initiator;
     *     if(sender instanceof Player) {
     *         initiator = new EconomyTransactionInitiator&#60;&#62;() {
     *             &#64;Override
     *             public Object getData() { return ((Player) sender).getUniqueId(); }
     *
     *             &#64;Override
     *             public &#64;NotNull Type getType() { return Type.PLAYER;}
     *         };
     *     } else {
     *         initiator = EconomyTransactionInitiator.SERVER;
     *     }
     *
     *     // Then we need to obtain the account.
     *     EconomySubscriber.asFuture(subscriber -&#62; economy.retrievePlayerAccount(target, subscriber))
     *
     *     // Then we set the balance.
     *     .thenCompose(account -&#62; EconomySubscriber.asFuture(subscriber -&#62; account.setBalance(balance, initiator, currency, subscriber)))
     *
     *     // And then we can use the final value however we like.
     *     .whenComplete((newBalance, exception) -&#62; {
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
     * @param subscriberConsumer a {@link Consumer} accepting an {@code EconomySubscriber}
     * @param <T>                the type of value expected by the {@code EconomySubscriber}
     * @return a future awaiting subscriber completion
     * @since v1.0.0
     */
    static <T> @NotNull CompletableFuture<T> asFuture(
            @NotNull Consumer<EconomySubscriber<T>>
                    subscriberConsumer
    ) {
        return Subscriber.asFuture((Subscriber<T, TreasuryException> subscriber) ->
                subscriberConsumer.accept(new EconomySubscriber<T>() {
                    @Override
                    public void succeed(@NotNull final T t) {
                        subscriber.succeed(t);
                    }

                    @Override
                    public void fail(@NotNull final EconomyException exception) {
                        subscriber.fail(exception);
                    }
                }));
    }
}
