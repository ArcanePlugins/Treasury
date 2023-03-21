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
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionImportance;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
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
import org.spongepowered.api.service.economy.account.VirtualAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

public class VirtualAccountImpl implements VirtualAccount {

    public static TransactionType SET_TRANSACTION_TYPE = RegistryKey
            .of(RegistryTypes.TRANSACTION_TYPE, ResourceKey.of("treasury", "set"))
            .asDefaultedReference(Sponge::game)
            .get();

    private final EconomyProvider delegateProvider;
    private final MappedCurrenciesCache mappedCurrenciesCache;
    protected me.lokka30.treasury.api.economy.account.Account delegateAccount;

    public VirtualAccountImpl(
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
        Response<BigDecimal> response = delegateAccount
                .retrieveBalance(migrationResult.getCurrency())
                .join();
        if (!response.isSuccessful()) {
            return BigDecimal.ZERO;
        }
        return response.getResult();
    }

    @Override
    public Map<Currency, BigDecimal> balances(final Set<Context> contexts) {
        return balances();
    }

    @Override
    public Map<Currency, BigDecimal> balances(final Cause cause) {
        Response<Collection<String>> heldCurrencies = delegateAccount
                .retrieveHeldCurrencies()
                .join();
        if (!heldCurrencies.isSuccessful()) {
            return Collections.emptyMap();
        }
        Map<Currency, BigDecimal> ret = new HashMap<>();
        for (String id : heldCurrencies.getResult()) {
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
                .withInitiator(EconomyTransactionInitiator.createInitiator(
                        EconomyTransactionInitiator.Type.SPECIAL,
                        cause.all()
                ))
                .withType(EconomyTransactionType.SET)
                .build());
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
        Response<Collection<String>> heldCurrencies = delegateAccount
                .retrieveHeldCurrencies()
                .join();
        if (!heldCurrencies.isSuccessful()) {
            return Collections.emptyMap();
        }
        EconomyTransactionInitiator<?> initiator = EconomyTransactionInitiator.createInitiator(
                EconomyTransactionInitiator.Type.SPECIAL,
                cause.all()
        );
        Map<Currency, TransactionResult> ret = new HashMap<>();
        for (String id : heldCurrencies.getResult()) {
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
                    initiator,
                    treasuryCurrency,
                    EconomyTransactionImportance.NORMAL
            );
            ret.put(sponge, new TransactionResultImpl(
                    this,
                    sponge,
                    treasuryCurrency instanceof SpongeToTreasuryCurrencyImpl ? BigDecimal.ZERO :
                            treasuryCurrency.getStartingBalance(this.delegateAccount),
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
        EconomyTransactionInitiator<?> initiator = EconomyTransactionInitiator.createInitiator(
                EconomyTransactionInitiator.Type.SPECIAL,
                cause.all()
        );
        me.lokka30.treasury.api.economy.currency.Currency treasuryCurrency = migrationResult.getCurrency();
        delegateAccount.resetBalance(
                initiator,
                treasuryCurrency,
                EconomyTransactionImportance.NORMAL
        );
        return new TransactionResultImpl(
                this,
                currency,
                treasuryCurrency instanceof SpongeToTreasuryCurrencyImpl ? BigDecimal.ZERO :
                        treasuryCurrency.getStartingBalance(this.delegateAccount),
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
        EconomyTransactionInitiator<?> initiator = EconomyTransactionInitiator.createInitiator(
                EconomyTransactionInitiator.Type.SPECIAL,
                cause.all()
        );
        me.lokka30.treasury.api.economy.currency.Currency treasuryCurrency = migrationResult.getCurrency();
        this.delegateAccount.depositBalance(
                amount,
                initiator,
                treasuryCurrency
        );
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
        EconomyTransactionInitiator<?> initiator = EconomyTransactionInitiator.createInitiator(
                EconomyTransactionInitiator.Type.SPECIAL,
                cause.all()
        );
        me.lokka30.treasury.api.economy.currency.Currency treasuryCurrency = migrationResult.getCurrency();
        this.delegateAccount.withdrawBalance(
                amount,
                initiator,
                treasuryCurrency
        );
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
        // TODO
        return null;
    }

    @Override
    public String identifier() {
        return this instanceof UniqueAccountImpl
                ? ((UniqueAccountImpl) this).uniqueId().toString()
                : ((NonPlayerAccount) this.delegateAccount).getIdentifier().toString();
    }

}
