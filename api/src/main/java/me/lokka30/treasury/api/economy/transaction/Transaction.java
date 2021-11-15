/*
 * Copyright (c) 2021-2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
