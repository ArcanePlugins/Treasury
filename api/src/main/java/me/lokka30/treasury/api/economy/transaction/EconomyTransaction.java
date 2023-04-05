/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Objects;
import java.util.Optional;
import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a transaction, made by an {@link me.lokka30.treasury.api.economy.account.Account}.
 * A transaction is a move on a specific entity's balance, whether it is a
 * {@link EconomyTransactionType#DEPOSIT},
 * {@link EconomyTransactionType#WITHDRAWAL} or {@link EconomyTransactionType#SET}.
 *
 * @author lokka30, MrIvanPlays
 * @since v1.0.0
 */
public class EconomyTransaction {

    /**
     * Creates a new {@link EconomyTransaction.Builder}
     *
     * @return new builder
     */
    @NotNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private final BigDecimal amount;
    private final String currencyId;
    private final Cause<?> cause;
    private final Instant timestamp;
    private final EconomyTransactionType type;
    private final Optional<String> reason;
    private final EconomyTransactionImportance importance;

    /**
     * Creates a new account transaction object.
     *
     * @param currencyId the currency's {@link Currency#getIdentifier() identifier} the transaction was made into
     * @param cause      the cause of the transaction. See {@link Cause}
     * @param timestamp  the time at which this transaction occurred. specifying null would mean "now"
     * @param type       the transaction type
     * @param reason     optional specification of a string message reason
     * @param amount     the amount which to deposit/withdraw
     * @since v1.0.0
     */
    public EconomyTransaction(
            @NotNull String currencyId,
            @NotNull Cause<?> cause,
            @Nullable Temporal timestamp,
            @NotNull EconomyTransactionType type,
            @Nullable String reason,
            @NotNull final BigDecimal amount,
            @NotNull EconomyTransactionImportance importance
    ) {
        this.currencyId = Objects.requireNonNull(currencyId, "currencyID");
        this.cause = Objects.requireNonNull(cause, "cause");
        this.type = Objects.requireNonNull(type, "transactionType");
        this.reason = Optional.ofNullable(reason);
        this.amount = amount;
        this.timestamp = timestamp == null
                ? Instant.now()
                : (timestamp instanceof Instant ? (Instant) timestamp : Instant.from(timestamp));
        this.importance = importance;
    }

    /**
     * Get the transaction amount.
     *
     * @return transaction amount
     * @since v1.0.0
     */
    @NotNull
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Returns the transaction type.
     *
     * @return transaction type
     * @since v1.0.0
     */
    @NotNull
    public EconomyTransactionType getType() {
        return type;
    }

    /**
     * Returns the {@link me.lokka30.treasury.api.economy.currency.Currency}'s {@link Currency#getIdentifier() identifier } with
     * which the transaction was made.
     *
     * <p>A {@code Currency} object is retrievable via {@link me.lokka30.treasury.api.economy.EconomyProvider#findCurrency(String)} if
     * you need such.
     *
     * @return The currency {@link Currency#getIdentifier() identifier}.
     * @since v1.0.0
     */
    @NotNull
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * Returns the {@link Cause} of this {@code EconomyTransaction}
     *
     * @return cause
     */
    @NotNull
    public Cause<?> getCause() {
        return cause;
    }

    /**
     * Returns the time at which this {@code Transaction} was made.
     *
     * @return timestamp
     */
    @NotNull
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the reason message of why this transaction happened.
     *
     * @return reason string or empty optional if not present
     */
    @NotNull
    public Optional<String> getReason() {
        return reason;
    }

    /**
     * Returns the importance of the transaction.
     *
     * @return importance
     */
    @NotNull
    public EconomyTransactionImportance getImportance() {
        return importance;
    }

    /**
     * Represents a builder of {@link EconomyTransaction}
     *
     * @author MrIvanPlays, lokka30
     */
    public static class Builder {

        private String currencyId;
        private Cause<?> cause;
        private Temporal timestamp;
        private EconomyTransactionType type;
        private String reason;
        private BigDecimal amount;
        private EconomyTransactionImportance importance;

        public Builder() {
        }

        /**
         * Creates a new {@link Builder} out of the specified {@code other}
         *
         * @param other the other builder to create a new builder from
         */
        public Builder(@NotNull Builder other) {
            this.currencyId = other.currencyId;
            this.cause = other.cause;
            this.timestamp = other.timestamp;
            this.type = other.type;
            this.reason = other.reason;
            this.amount = other.amount;
            this.importance = other.importance;
        }

        /**
         * Creates a copy of this {@code Builder}
         *
         * @return builder copy
         */
        @NotNull
        public Builder copy() {
            return new Builder(this);
        }

        /**
         * Specify the {@link Currency} the transaction was made in.
         *
         * @param currency currency
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withCurrency(@NotNull Currency currency) {
            this.currencyId = Objects.requireNonNull(currency, "currency").getIdentifier();
            return this;
        }

        /**
         * Specify the {@link Currency#getIdentifier() identifier} of a {@link Currency} the transaction was made in.
         *
         * @param currencyId currency id
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withCurrencyId(@NotNull String currencyId) {
            this.currencyId = Objects.requireNonNull(currencyId, "currencyId");
            return this;
        }

        /**
         * Specify the {@link Cause} the transaction got triggered by.
         *
         * @param cause cause
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withCause(@NotNull Cause<?> cause) {
            this.cause = Objects.requireNonNull(cause, "cause");
            return this;
        }

        /**
         * Specify the time when the transaction got triggered.
         *
         * @param timestamp timestamp
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withTimestamp(@Nullable Temporal timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Specify the {@link EconomyTransactionType} {@code transactionType} of the transaction.
         *
         * @param type transaction type
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withType(@NotNull EconomyTransactionType type) {
            this.type = Objects.requireNonNull(type, "type");
            return this;
        }

        /**
         * Specify a reason for this transaction.
         *
         * @param reason reason
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withReason(@Nullable String reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Specify the amount this transaction is going to either {@link EconomyTransactionType#WITHDRAWAL} or
         * {@link EconomyTransactionType#DEPOSIT}
         *
         * @param amount transaction amount
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withAmount(@NotNull BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Specify the importance this transaction should have.
         *
         * @param importance importance of the transaction
         * @return this insatance for chaining
         */
        @Contract("_ -> this")
        public Builder withImportance(@NotNull EconomyTransactionImportance importance) {
            this.importance = importance;
            return this;
        }

        /**
         * Builds the specified stuff into a new {@link EconomyTransaction}
         *
         * @return transaction object
         */
        @NotNull
        public EconomyTransaction build() {
            Objects.requireNonNull(currencyId, "currencyID");
            Objects.requireNonNull(cause, "cause");
            Objects.requireNonNull(type, "transactionType");
            Objects.requireNonNull(amount, "transactionAmount");
            Objects.requireNonNull(importance, "importance");
            return new EconomyTransaction(currencyId,
                    cause,
                    timestamp,
                    type,
                    reason,
                    amount,
                    importance
            );
        }

    }

}
