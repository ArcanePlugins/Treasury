/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.transaction;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Objects;
import java.util.Optional;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a transaction, made by an {@link me.lokka30.treasury.api.economy.account.Account}.
 * A transaction is a move on a specific entity's balance, whether it is a {@link EconomyTransactionType#DEPOSIT} or
 * {@link EconomyTransactionType#WITHDRAWAL}.
 *
 * @author lokka30, MrIvanPlays
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public class EconomyTransaction {

    /**
     * Creates a new {@link EconomyTransaction.Builder}
     *
     * @return new builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    private final double transactionAmount;
    @NotNull
    private final String currencyID;
    @NotNull
    private final EconomyTransactionInitiator<?> initiator;
    @NotNull
    private final Instant timestamp;
    @NotNull
    private final EconomyTransactionType economyTransactionType;
    private final Optional<String> reason;

    /**
     * Creates a new account transaction object.
     *
     * @param currencyID             the currency's {@link Currency#getIdentifier() identifier} the transaction was made into
     * @param initiator              the one who initiated the transaction
     * @param timestamp              the time at which this transaction occurred. specifying null would mean "now"
     * @param economyTransactionType the transaction type
     * @param reason                 optional specification of a string message reason
     * @param transactionAmount      the amount which to deposit/withdraw
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public EconomyTransaction(
            @NotNull String currencyID,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @Nullable Temporal timestamp,
            @NotNull EconomyTransactionType economyTransactionType,
            @Nullable String reason,
            final double transactionAmount
    ) {
        this.currencyID = Objects.requireNonNull(currencyID, "currencyID");
        this.initiator = Objects.requireNonNull(initiator, "initiator");
        this.economyTransactionType = Objects.requireNonNull(economyTransactionType,
                "transactionType"
        );
        this.reason = Optional.ofNullable(reason);
        this.transactionAmount = transactionAmount;
        this.timestamp = timestamp == null
                ? Instant.now()
                : (timestamp instanceof Instant ? (Instant) timestamp : Instant.from(timestamp));
    }

    /**
     * Get the transaction amount.
     *
     * @return transaction amount
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Returns the transaction type.
     *
     * @return transaction type
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    public EconomyTransactionType getTransactionType() {
        return economyTransactionType;
    }

    /**
     * Returns the {@link me.lokka30.treasury.api.economy.currency.Currency}'s {@link Currency#getIdentifier() identifier } with
     * which the transaction was made.
     *
     * <p>A {@code Currency} object is retrievable via {@link me.lokka30.treasury.api.economy.EconomyProvider#findCurrency(String)} if
     * you need such.
     *
     * @return The currency {@link Currency#getIdentifier() identifier}.
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    public String getCurrencyID() {
        return currencyID;
    }

    /**
     * Returns the {@link EconomyTransactionInitiator} of this {@code EconomyTransaction}
     *
     * @return initiator
     */
    @NotNull
    public EconomyTransactionInitiator<?> getInitiator() {
        return initiator;
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
    public Optional<String> getReason() {
        return reason;
    }

    /**
     * Represents a builder of {@link EconomyTransaction}
     *
     * @author MrIvanPlays
     */
    public static class Builder {

        private String currencyID;
        private EconomyTransactionInitiator<?> initiator;
        private Temporal timestamp;
        private EconomyTransactionType economyTransactionType;
        private String reason;
        private Double transactionAmount;

        public Builder() {
        }

        /**
         * Creates a new {@link Builder} out of the specified {@code other}
         *
         * @param other the other builder to create a new builder from
         */
        public Builder(Builder other) {
            this.currencyID = other.currencyID;
            this.initiator = other.initiator;
            this.timestamp = other.timestamp;
            this.economyTransactionType = other.economyTransactionType;
            this.reason = other.reason;
            this.transactionAmount = other.transactionAmount;
        }

        /**
         * Creates a copy of this {@code Builder}
         *
         * @return builder copy
         */
        public Builder copy() {
            return new Builder(this);
        }

        /**
         * Specify the {@link Currency} the transaction was made in.
         *
         * @param currency currency
         * @return this instance for chaining
         */
        public Builder withCurrency(@NotNull Currency currency) {
            this.currencyID = Objects.requireNonNull(currency, "currency").getIdentifier();
            return this;
        }

        /**
         * Specify the {@link Currency#getIdentifier() identifier} of a {@link Currency} the transaction was made in.
         *
         * @param currencyId currency id
         * @return this instance for chaining
         */
        public Builder withCurrencyId(@NotNull String currencyId) {
            this.currencyID = Objects.requireNonNull(currencyId, "currencyId");
            return this;
        }

        /**
         * Specify the {@link EconomyTransactionInitiator} the transaction got triggered by.
         *
         * @param initiator initiator
         * @return this instance for chaining
         */
        public Builder withInitiator(@NotNull EconomyTransactionInitiator<?> initiator) {
            this.initiator = Objects.requireNonNull(initiator, "initiator");
            return this;
        }

        /**
         * Specify the time when the transaction got triggered.
         *
         * @param timestamp timestamp
         * @return this instance for chaining
         */
        public Builder withTimestamp(@Nullable Temporal timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Specify the {@link EconomyTransactionType} {@code transactionType} of the transaction.
         *
         * @param economyTransactionType transaction type
         * @return this instance for chaining
         */
        public Builder withTransactionType(@NotNull EconomyTransactionType economyTransactionType) {
            this.economyTransactionType = Objects.requireNonNull(economyTransactionType,
                    "transactionType"
            );
            return this;
        }

        /**
         * Specify a reason for this transaction.
         *
         * @param reason reason
         * @return this instance for chaining
         */
        public Builder withReason(@Nullable String reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Specify the amount this transaction is going to either {@link EconomyTransactionType#WITHDRAWAL} or
         * {@link EconomyTransactionType#DEPOSIT}
         *
         * @param transactionAmount transaction amount
         * @return this instance for chaining
         */
        public Builder withTransactionAmount(double transactionAmount) {
            this.transactionAmount = transactionAmount;
            return this;
        }

        /**
         * Builds the specified stuff into a new {@link EconomyTransaction}
         *
         * @return transaction object
         */
        public EconomyTransaction build() {
            Objects.requireNonNull(currencyID, "currencyID");
            Objects.requireNonNull(initiator, "initiator");
            Objects.requireNonNull(economyTransactionType, "transactionType");
            Objects.requireNonNull(transactionAmount, "transactionAmount");
            return new EconomyTransaction(currencyID,
                    initiator,
                    timestamp,
                    economyTransactionType,
                    reason,
                    transactionAmount
            );
        }

    }

}
