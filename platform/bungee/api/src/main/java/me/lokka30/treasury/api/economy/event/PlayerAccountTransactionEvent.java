package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.event.AsyncEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event, called when a {@link PlayerAccount} does a {@link EconomyTransaction}
 *
 * @author MrIvanPlays, lokka30
 */
public class PlayerAccountTransactionEvent extends
        AsyncEvent<PlayerAccountTransactionEvent> implements Cancellable {

    private final PlayerAccount account;
    private final EconomyTransaction transaction;
    private boolean cancelled;

    public PlayerAccountTransactionEvent(
            @NotNull PlayerAccount account,
            @NotNull EconomyTransaction transaction,
            @NotNull Callback<PlayerAccountTransactionEvent> done
    ) {
        super(done);
        this.account = account;
        this.transaction = transaction;
    }

    /**
     * Returns the {@link PlayerAccount} which is doing the {@link #getTransaction()}
     *
     * @return account
     */
    @NotNull
    public PlayerAccount getAccount() {
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
