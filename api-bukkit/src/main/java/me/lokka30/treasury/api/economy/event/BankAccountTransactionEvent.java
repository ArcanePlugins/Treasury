/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.transaction.Transaction;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class BankAccountTransactionEvent extends AccountTransactionEvent {

    public BankAccountTransactionEvent(@NotNull Transaction transaction, @NotNull BankAccount account) {
        super(transaction, account);
    }

    @Override
    public @NotNull BankAccount getAccount() { return (BankAccount) super.getAccount(); }
    
    public static HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}