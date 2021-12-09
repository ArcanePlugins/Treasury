/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.transaction.Transaction;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerAccountTransactionEvent extends AccountTransactionEvent {

    public PlayerAccountTransactionEvent(@NotNull Transaction transaction, @NotNull PlayerAccount account) {
        super(transaction, account);
    }

    @Override
    public @NotNull PlayerAccount getAccount() { return (PlayerAccount) super.getAccount(); }
    
    public static HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
