package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.event.AsyncEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event, called when an {@link Account} does a {@link EconomyTransaction}
 *
 * @author MrIvanPlays, lokka30, MrNemo64
 */
public class AccountTransactionEvent extends AsyncEvent<AccountTransactionEvent> implements
        Cancellable {

    private final Account account;
    private final EconomyTransaction transaction;
    private boolean cancelled;

    public AccountTransactionEvent(
            @NotNull Account account,
            @NotNull EconomyTransaction transaction,
            @NotNull Callback<AccountTransactionEvent> done
    ) {
        super(done);
        this.account = account;
        this.transaction = transaction;
    }

    /**
     * Returns the {@link Account}, whether a player or a non-player one, which is doing the
     * {@link #getTransaction()}
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
        return transaction;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
