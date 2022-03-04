package me.lokka30.treasury.api.economy.events;

import me.lokka30.treasury.api.common.event.EventBus;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class EventCallingPlayerAccount extends EventCallingAccount<PlayerAccount> implements
        PlayerAccount {


    public EventCallingPlayerAccount(final PlayerAccount originalAccount, final EventBus eventBus) {
        super(originalAccount, eventBus);
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return getHandle().getUniqueId();
    }

    @Override
    public void resetBalance(
            @NotNull final EconomyTransactionInitiator<?> initiator,
            @NotNull final Currency currency,
            @NotNull final EconomySubscriber<BigDecimal> subscription
    ) {
        // todo wrap subscription for event
        getHandle().resetBalance(initiator, currency, subscription);
    }

}
