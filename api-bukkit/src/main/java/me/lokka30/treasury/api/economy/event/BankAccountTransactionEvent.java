/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event, called when a {@link BankAccount} does a {@link EconomyTransaction}
 *
 * @author lokka30
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public class BankAccountTransactionEvent extends AccountTransactionEvent {

    public BankAccountTransactionEvent(@NotNull EconomyTransaction economyTransaction, @NotNull BankAccount account) {
        super(economyTransaction, account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull BankAccount getAccount() {
        return (BankAccount) super.getAccount();
    }

    public static HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
