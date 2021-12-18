/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.GenericAccount;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event, called when a {@link GenericAccount} does a {@link EconomyTransaction}
 *
 * @author lokka30
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public class GenericAccountTransactionEvent extends AccountTransactionEvent {

    public GenericAccountTransactionEvent(@NotNull EconomyTransaction economyTransaction, @NotNull GenericAccount account) {
        super(economyTransaction, account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull GenericAccount getAccount() {
        return (GenericAccount) super.getAccount();
    }

    public static HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
