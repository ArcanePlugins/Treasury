/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionImportance;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.registry.RegistryKey;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

public abstract class AbstractAccountImpl implements Account {

    public static TransactionType SET_TRANSACTION_TYPE = RegistryKey
            .of(RegistryTypes.TRANSACTION_TYPE, ResourceKey.of("treasury", "set"))
            .asDefaultedReference(Sponge::game)
            .get();

    private final EconomyProvider delegateProvider;
    private final MappedCurrenciesCache mappedCurrenciesCache;
    protected me.lokka30.treasury.api.economy.account.Account delegateAccount;

    public AbstractAccountImpl(
            EconomyProvider delegateProvider,
            MappedCurrenciesCache mappedCurrenciesCache,
            me.lokka30.treasury.api.economy.account.Account delegateAccount
    ) {
        this.delegateProvider = delegateProvider;
        this.mappedCurrenciesCache = mappedCurrenciesCache;
        this.delegateAccount = delegateAccount;
    }

    @Override
    public Component displayName() {
        if (delegateAccount.getName().isPresent()) {
            return Component.text(delegateAccount.getName().get());
        }
        return null;
    }

    @Override
    public BigDecimal defaultBalance(final Currency currency) {
        if (currency instanceof SpongeCurrencyImpl) {
            return ((SpongeCurrencyImpl) currency)
                    .getDelegate()
                    .getStartingBalance(this.delegateAccount);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public boolean hasBalance(final Currency currency, final Set<Context> contexts) {
        return hasBalance(currency);
    }

    @Override
    public boolean hasBalance(final Currency currency, final Cause cause) {
        return balance(currency, cause).compareTo(BigDecimal.ONE) == 1;
    }

    @Override
    public BigDecimal balance(final Currency currency, final Set<Context> contexts) {
        return balance(currency);
    }

    @Override
    public BigDecimal balance(final Currency currency, final Cause cause) {
        MappedCurrenciesCache.MigrationResult migrationResult = mappedCurrenciesCache.migrateCurrency(
                currency);
        if (migrationResult.getState() == MappedCurrenciesCache.MigrationResult.State.JUST_CREATED) {
            return BigDecimal.ZERO;
        }
        return delegateAccount.retrieveBalance(migrationResult.getCurrency()).join();
    }

    @Override
    public Map<Currency, BigDecimal> balances(final Set<Context> contexts) {
        return balances();
    }

    @Override
    public Map<Currency, BigDecimal> balances(final Cause cause) {
        Collection<String> heldCurrencies = delegateAccount.retrieveHeldCurrencies().join();
        if (heldCurrencies.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Currency, BigDecimal> ret = new HashMap<>();
        for (String id : heldCurrencies) {
            me.lokka30.treasury.api.economy.currency.Currency treasuryCurrency = delegateProvider
                    .findCurrency(id)
                    .orElse(null);
            if (treasuryCurrency == null) {
                continue;
            }
            Currency sponge;
            if (treasuryCurrency instanceof SpongeToTreasuryCurrencyImpl) {
                sponge = ((SpongeToTreasuryCurrencyImpl) treasuryCurrency).getDelegateSpongeCurrency();
            } else {
                sponge = new SpongeCurrencyImpl(treasuryCurrency);
            }
            ret.put(sponge, this.balance(sponge));
        }
        return ret;
    }

    @Override
    public TransactionResult setBalance(
            final Currency currency, final BigDecimal amount, final Set<Context> contexts
    ) {
        return setBalance(currency, amount);
    }

    @Override
    public TransactionResult setBalance(
            final Currency currency, final BigDecimal amount, final Cause cause
    ) {
        me.lokka30.treasury.api.economy.currency.Currency treasuryCurrency = mappedCurrenciesCache
                .migrateCurrency(currency)
                .getCurrency();
        delegateAccount.doTransaction(EconomyTransaction
                .newBuilder()
                .withCurrency(treasuryCurrency)
                .withAmount(amount)
                .withImportance(EconomyTransactionImportance.NORMAL)
                .withCause(me.lokka30.treasury.api.common.Cause.SERVER)
                .withType(EconomyTransactionType.SET)
                .build()).join();
        return new TransactionResultImpl(
                this,
                currency,
                amount,
                SET_TRANSACTION_TYPE,
                ResultType.SUCCESS,
                new HashSet<>()
        );
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(final Set<Context> contexts) {
        return resetBalances();
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(final Cause cause) {
        Collection<String> heldCurrencies = delegateAccount.retrieveHeldCurrencies().join();
        if (heldCurrencies.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Currency, TransactionResult> ret = new HashMap<>();
        for (String id : heldCurrencies) {
            me.lokka30.treasury.api.economy.currency.Currency treasuryCurrency = delegateProvider
                    .findCurrency(id)
                    .orElse(null);
            if (treasuryCurrency == null) {
                continue;
            }
            Currency sponge;
            if (treasuryCurrency instanceof SpongeToTreasuryCurrencyImpl) {
                sponge = ((SpongeToTreasuryCurrencyImpl) treasuryCurrency).getDelegateSpongeCurrency();
            } else {
                sponge = new SpongeCurrencyImpl(treasuryCurrency);
            }
            delegateAccount.resetBalance(
                    me.lokka30.treasury.api.common.Cause.SERVER,
                    treasuryCurrency,
                    EconomyTransactionImportance.NORMAL
            ).join();
            ret.put(sponge, new TransactionResultImpl(
                    this,
                    sponge,
                    treasuryCurrency instanceof SpongeToTreasuryCurrencyImpl
                            ? BigDecimal.ZERO
                            : treasuryCurrency.getStartingBalance(this.delegateAccount),
                    SET_TRANSACTION_TYPE,
                    ResultType.SUCCESS,
                    new HashSet<>()
            ));
        }
        return ret;
    }

    @Override
    public TransactionResult resetBalance(final Currency currency, final Set<Context> contexts) {
        return resetBalance(currency);
    }

    @Override
    public TransactionResult resetBalance(final Currency currency, final Cause cause) {
        MappedCurrenciesCache.MigrationResult migrationResult = mappedCurrenciesCache.migrateCurrency(
                currency);
        if (migrationResult.getState() == MappedCurrenciesCache.MigrationResult.State.JUST_CREATED) {
            return new TransactionResultImpl(
                    this,
                    currency,
                    BigDecimal.ZERO,
                    SET_TRANSACTION_TYPE,
                    ResultType.FAILED,
                    new HashSet<>()
            );
        }
        me.lokka30.treasury.api.economy.currency.Currency treasuryCurrency = migrationResult.getCurrency();
        delegateAccount.resetBalance(
                me.lokka30.treasury.api.common.Cause.SERVER,
                treasuryCurrency,
                EconomyTransactionImportance.NORMAL
        ).join();
        return new TransactionResultImpl(
                this,
                currency,
                treasuryCurrency instanceof SpongeToTreasuryCurrencyImpl
                        ? BigDecimal.ZERO
                        : treasuryCurrency.getStartingBalance(this.delegateAccount),
                SET_TRANSACTION_TYPE,
                ResultType.FAILED,
                new HashSet<>()
        );
    }

    @Override
    public TransactionResult deposit(
            final Currency currency, final BigDecimal amount, final Set<Context> contexts
    ) {
        return deposit(currency, amount);
    }

    @Override
    public TransactionResult deposit(
            final Currency currency, final BigDecimal amount, final Cause cause
    ) {
        MappedCurrenciesCache.MigrationResult migrationResult = mappedCurrenciesCache.migrateCurrency(
                currency);
        me.lokka30.treasury.api.economy.currency.Currency treasuryCurrency = migrationResult.getCurrency();
        this.delegateAccount.depositBalance(
                amount,
                me.lokka30.treasury.api.common.Cause.SERVER,
                treasuryCurrency
        ).join();
        return new TransactionResultImpl(
                this,
                currency,
                amount,
                TransactionTypes.DEPOSIT.get(),
                ResultType.SUCCESS,
                new HashSet<>()
        );
    }

    @Override
    public TransactionResult withdraw(
            final Currency currency, final BigDecimal amount, final Set<Context> contexts
    ) {
        return withdraw(currency, amount);
    }

    @Override
    public TransactionResult withdraw(
            final Currency currency, final BigDecimal amount, final Cause cause
    ) {
        MappedCurrenciesCache.MigrationResult migrationResult = mappedCurrenciesCache.migrateCurrency(
                currency);
        me.lokka30.treasury.api.economy.currency.Currency treasuryCurrency = migrationResult.getCurrency();
        this.delegateAccount.withdrawBalance(
                amount,
                me.lokka30.treasury.api.common.Cause.SERVER,
                treasuryCurrency
        ).join();
        return new TransactionResultImpl(
                this,
                currency,
                amount,
                TransactionTypes.WITHDRAW.get(),
                ResultType.SUCCESS,
                new HashSet<>()
        );
    }

    @Override
    public TransferResult transfer(
            final Account to,
            final Currency currency,
            final BigDecimal amount,
            final Set<Context> contexts
    ) {
        return transfer(to, currency, amount);
    }

    @Override
    public TransferResult transfer(
            final Account to, final Currency currency, final BigDecimal amount, final Cause cause
    ) {
        MappedCurrenciesCache.MigrationResult migrationResult = mappedCurrenciesCache.migrateCurrency(
                currency);
        me.lokka30.treasury.api.economy.currency.Currency treasuryCurrency = migrationResult.getCurrency();
        me.lokka30.treasury.api.economy.account.Account toDelegate = null;
        if (to instanceof AbstractAccountImpl) {
            toDelegate = ((AbstractAccountImpl) to).delegateAccount;
        }
        if (toDelegate == null) {
            // what
            UUID uuid = null;
            NamespacedKey id = null;
            try {
                uuid = UUID.fromString(to.identifier());
            } catch (IllegalArgumentException e) {
                id = NamespacedKey.fromString(to.identifier());
            }
            if (uuid != null) {
                toDelegate = delegateProvider
                        .accountAccessor()
                        .player()
                        .withUniqueId(uuid)
                        .get()
                        .join();
            } else {
                toDelegate = delegateProvider
                        .accountAccessor()
                        .nonPlayer()
                        .withIdentifier(id)
                        .get()
                        .join();
            }
            if (toDelegate == null) {
                throw new IllegalArgumentException(
                        "Failed to recover from null delegate: accountId = " + to.identifier());
            }
        }
        delegateAccount
                .withdrawBalance(
                        amount,
                        (me.lokka30.treasury.api.common.Cause<?>) delegateAccount,
                        treasuryCurrency
                )
                .join();
        toDelegate
                .depositBalance(
                        amount,
                        (me.lokka30.treasury.api.common.Cause<?>) delegateAccount,
                        treasuryCurrency
                )
                .join();
        return new TransferResultImpl(
                this,
                to,
                currency,
                amount,
                TransactionTypes.TRANSFER.get(),
                ResultType.SUCCESS,
                new HashSet<>()
        );
    }

}
