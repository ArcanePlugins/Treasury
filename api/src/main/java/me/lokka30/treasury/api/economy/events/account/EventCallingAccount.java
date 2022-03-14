package me.lokka30.treasury.api.economy.events.account;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import me.lokka30.treasury.api.common.event.EventBus;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Decorating class for event calling account
 */
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
        getHandle().setName(name, new EconomySubscriber<Boolean>() {
            @Override
            public void succeed(@NotNull final Boolean aBoolean) {
                subscription.succeed(aBoolean);
                // fire event
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

    @Override
    public void retrieveBalance(
            @NotNull final Currency currency,
            @NotNull final EconomySubscriber<BigDecimal> subscription
    ) {
        getHandle().retrieveBalance(currency, new EconomySubscriber<BigDecimal>() {
            @Override
            public void succeed(@NotNull final BigDecimal bigDecimal) {
                subscription.succeed(bigDecimal);
                // todo fire event
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

    @Override
    public void setBalance(
            @NotNull final BigDecimal amount,
            @NotNull final EconomyTransactionInitiator<?> initiator,
            @NotNull final Currency currency,
            @NotNull final EconomySubscriber<BigDecimal> subscription
    ) {
        getHandle().setBalance(amount, initiator, currency, new EconomySubscriber<BigDecimal>() {
            @Override
            public void succeed(@NotNull final BigDecimal bigDecimal) {
                subscription.succeed(bigDecimal);
                // todo fire event
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

    @Override
    public void doTransaction(
            @NotNull final EconomyTransaction economyTransaction,
            @NotNull final EconomySubscriber<BigDecimal> subscription
    ) {
        getHandle().doTransaction(economyTransaction, new EconomySubscriber<BigDecimal>() {
            @Override
            public void succeed(@NotNull final BigDecimal bigDecimal) {
                subscription.succeed(bigDecimal);
                // todo fire event
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

    @Override
    public void deleteAccount(@NotNull final EconomySubscriber<Boolean> subscription) {
        getHandle().deleteAccount(new EconomySubscriber<Boolean>() {
            @Override
            public void succeed(@NotNull final Boolean aboolean) {
                subscription.succeed(aboolean);
                // todo fire event
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
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
        getHandle().retrieveTransactionHistory(
                transactionCount,
                from,
                to,
                new EconomySubscriber<Collection<EconomyTransaction>>() {
                    @Override
                    public void succeed(@NotNull final Collection<EconomyTransaction> economyTransactions) {
                        subscription.succeed(economyTransactions);
                        // fire event
                    }
                    @Override
                    public void fail(@NotNull final EconomyException exception) {
                        subscription.fail(exception);
                    }
                }
        );
    }

    @Override
    public void retrieveMemberIds(@NotNull final EconomySubscriber<Collection<UUID>> subscription) {
        getHandle().retrieveMemberIds(new EconomySubscriber<Collection<UUID>>() {
            @Override
            public void succeed(@NotNull final Collection<UUID> uuids) {
                subscription.succeed(uuids);
                // fire event
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

    @Override
    public void isMember(
            @NotNull final UUID player,
            @NotNull final EconomySubscriber<Boolean> subscription
    ) {
        getHandle().isMember(player, new EconomySubscriber<Boolean>() {
            @Override
            public void succeed(@NotNull final Boolean aBoolean) {
                subscription.succeed(aBoolean);
                // fire event
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

    @Override
    public void setPermission(
            @NotNull final UUID player,
            @NotNull final TriState permissionValue,
            @NotNull final EconomySubscriber<TriState> subscription,
            final @NotNull AccountPermission @NotNull ... permissions
    ) {
        getHandle().setPermission(player, permissionValue, new EconomySubscriber<TriState>() {
            @Override
            public void succeed(@NotNull final TriState triState) {
                subscription.succeed(triState);
                // todo fire event
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        }, permissions);
    }

    @Override
    public void retrievePermissions(
            @NotNull final UUID player,
            @NotNull final EconomySubscriber<Map<AccountPermission, TriState>> subscription
    ) {
        getHandle().retrievePermissions(
                player,
                new EconomySubscriber<Map<AccountPermission, TriState>>() {
                    @Override
                    public void succeed(@NotNull final Map<AccountPermission, TriState> accountPermissionTriStateMap) {
                        subscription.succeed(accountPermissionTriStateMap);
                        // todo fire event
                    }

                    @Override
                    public void fail(@NotNull final EconomyException exception) {
                        subscription.fail(exception);
                    }
                }
        );
    }

    @Override
    public void hasPermission(
            @NotNull final UUID player,
            @NotNull final EconomySubscriber<TriState> subscription,
            final @NotNull AccountPermission @NotNull ... permissions
    ) {
        getHandle().hasPermission(player, new EconomySubscriber<TriState>() {
            @Override
            public void succeed(@NotNull final TriState triState) {
                subscription.succeed(triState);
                // todo fire event, when some is present
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        }, permissions);
    }

    protected T getHandle() {
        return this.originalAccount;
    }
}
