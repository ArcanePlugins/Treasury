package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.event.AsyncEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event, called when a {@link NonPlayerAccount} does a {@link EconomyTransaction}
 *
 * @author MrIvanPlays, lokka30
 */
public class NonPlayerAccountTransactionEvent extends
        AsyncEvent<NonPlayerAccountTransactionEvent> implements Cancellable {

    private final NonPlayerAccount account;
    private final EconomyTransaction transaction;
    private boolean cancelled;

    public NonPlayerAccountTransactionEvent(
            @NotNull NonPlayerAccount account,
            @NotNull EconomyTransaction transaction,
            @NotNull Callback<NonPlayerAccountTransactionEvent> done
    ) {
        super(done);
        this.account = account;
        this.transaction = transaction;
    }

    /**
     * Returns the {@link NonPlayerAccount} which is doing the {@link #getTransaction()}
     *
     * @return account
     */
    @NotNull
    public NonPlayerAccount getAccount() {
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
