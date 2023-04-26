/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.events;

import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event, called when an account does a {@link EconomyTransaction}
 *
 * @author lokka30, MrNemo64, MrIvanPlays
 * @since v1.1.0
 */
public class AccountTransactionEvent {

    @NotNull
    private final EconomyTransaction economyTransaction;
    @NotNull
    private final Account account;

    public AccountTransactionEvent(
            @NotNull EconomyTransaction economyTransaction, @NotNull Account account
    ) {
        this.economyTransaction = economyTransaction;
        this.account = account;
    }

    /**
     * Returns the {@link Account} for this account event.
     *
     * @return account
     */
    @NotNull
    public Account getAccount() {
        return account;
    }

    /**
     * Returns the {@link EconomyTransaction} the {@link #getAccount()} is doing.
     *
     * @return transaction
     */
    @NotNull
    public EconomyTransaction getTransaction() {
        return economyTransaction;
    }

}
