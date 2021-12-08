/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.transaction.Transaction;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class AccountTransactionEvent extends AccountEvent implements Cancellable {

    @NotNull private final Transaction transaction;
    private boolean isCancelled = false;

    public AccountTransactionEvent(@NotNull final Transaction transaction, @NotNull final Account account) {
    	  super(account);
        this.transaction = transaction;
    }

    @NotNull
    public Transaction getTransaction() { return transaction; }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public static HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
