package me.lokka30.treasury.api.economy.events.account;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import me.lokka30.treasury.api.common.event.EventBus;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.events.AccountTransactionEvent;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionImportance;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import org.jetbrains.annotations.NotNull;

/**
 * Decorating class for event calling player account
 */
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
        getHandle().resetBalance(initiator, currency, new EconomySubscriber<BigDecimal>() {
            @Override
            public void succeed(@NotNull final BigDecimal bigDecimal) {
                subscription.succeed(bigDecimal);
                eventBus.fire(new AccountTransactionEvent(new EconomyTransaction(
                        currency.getIdentifier(),
                        initiator,
                        Instant.now(),
                        EconomyTransactionType.WITHDRAWAL,
                        "Balance reset",
                        bigDecimal,
                        EconomyTransactionImportance.HIGH
                ), EventCallingPlayerAccount.this));
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

}
