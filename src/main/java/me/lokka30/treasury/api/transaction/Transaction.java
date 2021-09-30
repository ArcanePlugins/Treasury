/*
 * Copyright (c) 2021 lokka30.
 * This code is part of Treasury, an Economy API for Minecraft servers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You have received a copy of the GNU Affero General Public License
 * with this program - please see the LICENSE.md file. Alternatively,
 * please visit the <https://www.gnu.org/licenses/> website.
 *
 * Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 */

package me.lokka30.treasury.api.transaction;

import me.lokka30.treasury.plugin.misc.Utils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class Transaction {

    @NotNull private final BigDecimal newBalance;
    @NotNull private final BigDecimal transactionAmount;
    @NotNull private final TransactionType transactionType;

    public Transaction(@NotNull final BigDecimal newBalance, @NotNull final BigDecimal transactionAmount, @NotNull final TransactionType transactionType) {
        this.newBalance = Utils.ensureNonZero(newBalance);
        this.transactionAmount = Utils.ensureNonZero(newBalance);
        this.transactionType = transactionType;
    }

    @NotNull
    public BigDecimal getNewBalance() { return newBalance; }

    @NotNull
    public BigDecimal getPreviousBalance() {
        switch(transactionType) {
            case DEPOSIT:
                return newBalance.subtract(transactionAmount);
            case WITHDRAWAL:
                return newBalance.add(transactionAmount);
            default:
                throw new IllegalStateException("Unexpected state " + transactionType);
        }
    }

    @NotNull
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    @NotNull
    public TransactionType getTransactionType() {
        return transactionType;
    }

}
