package me.lokka30.treasury.api.economy.events;

import me.lokka30.treasury.api.common.event.EventBus;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class EventCallingAccount<T extends Account> implements Account {

    private final T originalAccount;
    protected final EventBus eventBus;

    public EventCallingAccount(final T originalAccount, EventBus eventBus) {
        this.originalAccount = originalAccount;
        this.eventBus = eventBus;
    }

    @Override
    public @NotNull String getIdentifier() {
        return getHandle().getIdentifier();
    }

    @Override
    public Optional<String> getName() {
        return getHandle().getName();
    }

    @Override
    public void setName(
            @Nullable final String name,
            @NotNull final EconomySubscriber<Boolean> subscription
    ) {
        getHandle().setName(name, subscription);
    }

    @Override
    public void retrieveBalance(
            @NotNull final Currency currency,
            @NotNull final EconomySubscriber<BigDecimal> subscription
    ) {
            getHandle().retrieveBalance(currency, subscription);
    }

    @Override
    public void setBalance(
            @NotNull final BigDecimal amount,
            @NotNull final EconomyTransactionInitiator<?> initiator,
            @NotNull final Currency currency,
            @NotNull final EconomySubscriber<BigDecimal> subscription
    ) {
        // todo wrap subscription for event
        getHandle().setBalance(amount, initiator, currency, subscription);
    }

    @Override
    public void doTransaction(
            @NotNull final EconomyTransaction economyTransaction,
            final EconomySubscriber<BigDecimal> subscription
    ) {
        // todo wrap subscription for event
        getHandle().doTransaction(economyTransaction, subscription);
    }

    @Override
    public void deleteAccount(@NotNull final EconomySubscriber<Boolean> subscription) {
        // todo wrap subscription for event
        getHandle().deleteAccount(subscription);
    }

    @Override
    public void retrieveHeldCurrencies(@NotNull final EconomySubscriber<Collection<String>> subscription) {
        getHandle().retrieveHeldCurrencies(subscription);
    }

    @Override
    public void retrieveTransactionHistory(
            final int transactionCount,
            @NotNull final Temporal from,
            @NotNull final Temporal to,
            @NotNull final EconomySubscriber<Collection<EconomyTransaction>> subscription
    ) {
        getHandle().retrieveTransactionHistory(transactionCount, from, to, subscription);
    }

    @Override
    public void retrieveMemberIds(@NotNull final EconomySubscriber<Collection<UUID>> subscription) {
        getHandle().retrieveMemberIds(subscription);
    }

    @Override
    public void isMember(
            @NotNull final UUID player,
            @NotNull final EconomySubscriber<Boolean> subscription
    ) {
        getHandle().isMember(player, subscription);
    }

    @Override
    public void setPermission(
            @NotNull final UUID player,
            @NotNull final TriState permissionValue,
            @NotNull final EconomySubscriber<TriState> subscription,
            final @NotNull AccountPermission @NotNull ... permissions
    ) {
        // todo wrap subscription for event
        getHandle().setPermission(player, permissionValue, subscription, permissions);
    }

    @Override
    public void retrievePermissions(
            @NotNull final UUID player,
            @NotNull final EconomySubscriber<Map<AccountPermission, TriState>> subscription
    ) {
        getHandle().retrievePermissions(player, subscription);
    }

    @Override
    public void hasPermission(
            @NotNull final UUID player,
            @NotNull final EconomySubscriber<TriState> subscription,
            final @NotNull AccountPermission @NotNull ... permissions
    ) {
        getHandle().hasPermission(player, subscription, permissions);
    }

    protected T getHandle() {
        return this.originalAccount;
    }
}
