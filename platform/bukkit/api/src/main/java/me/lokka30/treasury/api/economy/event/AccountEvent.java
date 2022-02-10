/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.Account;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event, holding a {@link Account} of some type.
 *
 * @author MrNemo64
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 * @deprecated use {@link me.lokka30.treasury.api.economy.events.AccountTransactionEvent}
 */
@Deprecated
public class AccountEvent extends Event {

    @NotNull
    private final Account account;

    public AccountEvent(@NotNull final Account account, boolean async) {
        super(async);
        this.account = account;
    }

    /**
     * Returns the {@link Account} for this account event.
     *
     * @return account
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    public Account getAccount() {
        return account;
    }

    public static HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
