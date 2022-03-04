package me.lokka30.treasury.api.economy.events.account;

import me.lokka30.treasury.api.common.event.EventBus;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;

/**
 * Decorating class for event calling non player account
 */
public class EventCallingNonPlayerAccount extends EventCallingAccount<NonPlayerAccount> implements NonPlayerAccount{
    public EventCallingNonPlayerAccount(
            final NonPlayerAccount originalAccount,
            final EventBus eventBus
    ) {
        super(originalAccount, eventBus);
    }
}
