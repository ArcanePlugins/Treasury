/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.transaction;

import me.lokka30.treasury.api.economy.misc.AmountUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Transaction {

    private final double newBalance;
    private final double transactionAmount;
    @NotNull private final TransactionType transactionType;

    public Transaction(final double newBalance, final double transactionAmount, @NotNull final TransactionType transactionType) {
        this.newBalance = AmountUtils.ensureAtLeastZero(newBalance);
        this.transactionAmount = AmountUtils.ensureAtLeastZero(newBalance);
        this.transactionType = transactionType;
    }

    public double getNewBalance() { return newBalance; }

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

    public double getTransactionAmount() {
        return transactionAmount;
    }

    @NotNull
    public TransactionType getTransactionType() {
        return transactionType;
    }

}
