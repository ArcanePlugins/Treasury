/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.Account;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AccountEvent extends Event {

	@NotNull private final Account account;
	
	public AccountEvent(@NotNull final Account account) {
		  this.account = account;
	}
	
	@NotNull
	public Account getAccount() { return account; }

	public static HandlerList HANDLERS = new HandlerList();

	@NotNull
	@Override
	public HandlerList getHandlers() {
		  return HANDLERS;
	}

}
