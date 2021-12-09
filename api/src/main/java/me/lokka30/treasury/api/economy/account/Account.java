/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An Account is something that holds a balance and is associated with
 * something bound by a UUID. For example, a PlayerAccount is bound to
 * a Player on a server by their UUID.
 *
 * @author lokka30, Geolykt
 * @see EconomyProvider
 * @see PlayerAccount
 * @see BankAccount
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface Account {

    /**
     * Get the {@link UUID} of the {@code Account}.
     *
     * @return uuid of the Account.
     * @author lokka30
     * @see UUID
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull UUID getUniqueId();

    /**
     * Request the balance of the {@code Account}.
     *
     * @param currency     the {@link Currency} of the balance being requested
     * @param subscription the {@link EconomySubscriber} accepting the amount
     * @author lokka30, Geolykt
     * @see Account#setBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveBalance(@NotNull Currency currency, @NotNull EconomySubscriber<Double> subscription);

    /**
     * Set the balance of the {@code Account}.
     *
     * <p>Specified amounts must be AT OR ABOVE zero.
     *
     * @param amount       the amount the new balance will be
     * @param initiator    the one who initiated the transaction
     * @param currency     the {@link Currency} of the balance being set
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @author lokka30, Geolykt, MrIvanPlays
     * @see Account#retrieveBalance(Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void setBalance(
            double amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomySubscriber<Double> subscription
    );

    /**
     * Withdraw an amount from the {@code Account} balance.
     *
     * <p>Specified amounts must be ABOVE zero.
     *
     * @param amount       the amount the balance will be reduced by
     * @param initiator    the one who initiated the transaction
     * @param currency     the {@link Currency} of the balance being modified
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @author lokka30, Geolykt, MrIvanPlays
     * @see Account#setBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @see Account#doTransaction(EconomyTransaction, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void withdrawBalance(
            double amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomySubscriber<Double> subscription
    ) {
        withdrawBalance(amount, initiator, currency, null, subscription);
    }

    /**
     * Withdraw an amount from the {@code Account} balance.
     *
     * <p>Specified amounts must be ABOVE zero.
     *
     * @param amount       the amount the balance will be reduced by
     * @param initiator    the one who initiated the transaction
     * @param currency     the {@link Currency} of the balance being modified
     * @param reason       the reason of why the balance is modified
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @author MrIvanPlays
     * @see Account#setBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @see Account#doTransaction(EconomyTransaction, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void withdrawBalance(
            double amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @Nullable String reason,
            @NotNull EconomySubscriber<Double> subscription
    ) {
        doTransaction(EconomyTransaction
                .newBuilder()
                .withCurrency(currency)
                .withInitiator(initiator)
                .withReason(reason)
                .withTransactionAmount(amount)
                .withTransactionType(EconomyTransactionType.WITHDRAWAL)
                .build(), subscription);
    }

    /**
     * Deposit an amount into the {@code Account} balance.
     *
     * <p>Specified amounts must be ABOVE zero.
     *
     * @param amount       the amount the balance will be increased by
     * @param initiator    the one who initiated the transaction
     * @param currency     the {@link Currency} of the balance being modified
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @author lokka30, MrIvanPlays
     * @see Account#setBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @see Account#doTransaction(EconomyTransaction, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void depositBalance(
            double amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomySubscriber<Double> subscription
    ) {
        depositBalance(amount, initiator, currency, null, subscription);
    }

    /**
     * Deposit an amount into the {@code Account} balance.
     *
     * <p>Specified amounts must be ABOVE zero.
     *
     * @param amount       the amount the balance will be increased by
     * @param initiator    the one who initiated the transaction
     * @param currency     the {@link Currency} of the balance being modified
     * @param reason       the reason of why the balance is modified
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @author MrIvanPlays
     * @see Account#setBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @see Account#doTransaction(EconomyTransaction, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void depositBalance(
            double amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @Nullable String reason,
            @NotNull EconomySubscriber<Double> subscription
    ) {
        doTransaction(EconomyTransaction
                .newBuilder()
                .withCurrency(currency)
                .withInitiator(initiator)
                .withTransactionAmount(amount)
                .withReason(reason)
                .withTransactionType(EconomyTransactionType.DEPOSIT)
                .build(), subscription);
    }

    /**
     * Does a {@link EconomyTransaction} on this account.
     *
     * @param economyTransaction the transaction that should be done
     * @param subscription       the {@link EconomySubscriber} accepting the new balance
     * @author MrIvanPlays
     * @see Account#setBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void doTransaction(@NotNull EconomyTransaction economyTransaction, EconomySubscriber<Double> subscription);

    /**
     * Reset the {@code Account} balance to its starting amount.
     *
     * <p>Certain implementations, such as the {@link PlayerAccount}, may default to non-zero starting balances.
     *
     * @param initiator    the one who initiated the transaction
     * @param currency     the {@link Currency} of the balance being reset
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @author lokka30, Geolykt, MrIvanPlays
     * @see PlayerAccount#resetBalance(EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @see Account#setBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void resetBalance(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomySubscriber<Double> subscription
    ) {
        setBalance(0.0d, initiator, currency, new EconomySubscriber<Double>() {
            @Override
            public void succeed(@NotNull Double value) {
                subscription.succeed(0.0d);
            }

            @Override
            public void fail(@NotNull EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

    /**
     * Check if the {@code Account} can afford a withdrawal of a certain amount.
     *
     * <p>Specified amounts must be ABOVE zero.
     *
     * @param amount       the amount the balance must meet or exceed
     * @param currency     the {@link Currency} of the balance being queried
     * @param subscription the {@link EconomySubscriber} accepting whether the balance is high enough
     * @author lokka30, Geolykt
     * @see Account#retrieveBalance(Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void canAfford(double amount, @NotNull Currency currency, @NotNull EconomySubscriber<Boolean> subscription) {
        retrieveBalance(currency, new EconomySubscriber<Double>() {
            @Override
            public void succeed(@NotNull Double value) {
                subscription.succeed(value >= amount);
            }

            @Override
            public void fail(@NotNull EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

    /**
     * Delete data stored for the {@code Account}.
     *
     * <p>Providers should consider storing backups of deleted accounts.
     *
     * @param subscription the {@link EconomySubscriber} accepting whether deletion occurred successfully
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void deleteAccount(@NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Returns the {@link Currency Currencies} this {@code Account} holds balance for.
     *
     * @param subscription the {@link EconomySubscriber} accepting the currencies
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveHeldCurrencies(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request the {@link EconomyTransaction} history, limited by the {@code transactionCount}, of this {@code Account}.
     *
     * <p>If the specified {@code transactionCount} is higher than the known transactions, then this will return all the
     * transactions.
     * <p>If the specified {@code transactionCount} is {@code -1}, then this will return all the transactions. <i>If you
     * really need all the transactions, please use this with care, because an economy provider might do a query to a database
     * .</i>
     *
     * @param transactionCount the count of the transactions wanted
     * @param subscription     the {@link EconomySubscriber} accepting the transaction history
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveTransactionHistory(
            int transactionCount, @NotNull EconomySubscriber<Collection<EconomyTransaction>> subscription
    );

    /**
     * Request the {@link EconomyTransaction} history, starting from the {@link Temporal} {@code from} specified, of this
     * {@code Account}.
     *
     * @param from         the timestamp to get the transactions from
     * @param subscription the {@link EconomySubscriber} accepting the transaction history
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void retrieveTransactionHistory(
            @NotNull Temporal from, @NotNull EconomySubscriber<Collection<EconomyTransaction>> subscription
    ) {
        retrieveTransactionHistory(from, Instant.now(), subscription);
    }

    /**
     * Request the {@link EconomyTransaction} history, starting from the {@link Temporal} {@code from} specified and
     * ending at {@link Temporal} {@code to} specified, of this {@code Account}.
     *
     * @param from         the timestamp to get the transactions from
     * @param to           the timestamp to get the transactions to
     * @param subscription the {@link EconomySubscriber} accepting the transaction history
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void retrieveTransactionHistory(
            @NotNull Temporal from, @NotNull Temporal to, @NotNull EconomySubscriber<Collection<EconomyTransaction>> subscription
    ) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Objects.requireNonNull(subscription, "subscription");

        Instant fromInstant;
        if (from instanceof Instant) {
            fromInstant = (Instant) from;
        } else {
            fromInstant = Instant.from(from);
        }
        Instant toInstant;
        if (to instanceof Instant) {
            toInstant = (Instant) to;
        } else {
            toInstant = Instant.from(to);
        }
        retrieveTransactionHistory(-1, new EconomySubscriber<Collection<EconomyTransaction>>() {
            @Override
            public void succeed(@NotNull final Collection<EconomyTransaction> economyTransactions) {
                subscription.succeed(economyTransactions.stream().filter(transaction -> {
                    Instant timestamp = transaction.getTimestamp();
                    return !timestamp.isBefore(fromInstant) && !timestamp.isAfter(toInstant);
                }).collect(Collectors.toList()));
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

}
