/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.transaction;

import me.lokka30.treasury.api.economy.misc.AmountUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a transaction. A transaction is a move on a specific entity's balance, whether it is
 * a {@link TransactionType#DEPOSIT} or {@link TransactionType#WITHDRAWAL}.
 *
 * @author lokka30
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
@SuppressWarnings("unused")
public class Transaction {

    private final double newBalance;
    private final double transactionAmount;
    @NotNull private final TransactionType transactionType;

    /**
     * Creates a new transaction object.
     *
     * @param newBalance the new balance to set
     * @param transactionAmount the amount which to deposit/withdraw
     * @param transactionType the transaction type
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public Transaction(final double newBalance, final double transactionAmount, @NotNull final TransactionType transactionType) {
        this.newBalance = AmountUtils.ensureAtLeastZero(newBalance);
        this.transactionAmount = AmountUtils.ensureAtLeastZero(newBalance);
        this.transactionType = transactionType;
    }

    /**
     * Get the new balance.
     *
     * @return new balance
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public double getNewBalance() { return newBalance; }

    /**
     * Get the previous balance.
     *
     * @return previous balance
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public double getPreviousBalance() {
        switch(transactionType) {
            case DEPOSIT:
                return newBalance - transactionAmount;
            case WITHDRAWAL:
                return newBalance + transactionAmount;
            default:
                throw new IllegalStateException("Unexpected state " + transactionType);
        }
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
    public TransactionType getTransactionType() {
        return transactionType;
    }

}
