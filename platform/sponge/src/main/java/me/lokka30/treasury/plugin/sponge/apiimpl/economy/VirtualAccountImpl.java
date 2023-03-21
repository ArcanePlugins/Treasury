/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import me.lokka30.treasury.api.economy.EconomyProvider;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.VirtualAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransferResult;

public class VirtualAccountImpl implements VirtualAccount {

    private final EconomyProvider delegateProvider;
    protected me.lokka30.treasury.api.economy.account.Account delegateAccount;

    public VirtualAccountImpl(EconomyProvider delegateProvider,
                              me.lokka30.treasury.api.economy.account.Account delegateAccount) {
        this.delegateProvider = delegateProvider;
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
            return ((SpongeCurrencyImpl) currency).getDelegate().getStartingBalance(this.delegateAccount);
        }
        return null; // TODO: find how to deal with currencies coming from the sponge api because
        //                    they do not have an identifier
    }

    @Override
    public boolean hasBalance(final Currency currency, final Set<Context> contexts) {
        return false;
    }

    @Override
    public boolean hasBalance(final Currency currency, final Cause cause) {
        return false;
    }

    @Override
    public BigDecimal balance(final Currency currency, final Set<Context> contexts) {
        return null;
    }

    @Override
    public BigDecimal balance(final Currency currency, final Cause cause) {
        return null;
    }

    @Override
    public Map<Currency, BigDecimal> balances(final Set<Context> contexts) {
        return null;
    }

    @Override
    public Map<Currency, BigDecimal> balances(final Cause cause) {
        return null;
    }

    @Override
    public TransactionResult setBalance(
            final Currency currency,
            final BigDecimal amount,
            final Set<Context> contexts
    ) {
        return null;
    }

    @Override
    public TransactionResult setBalance(
            final Currency currency,
            final BigDecimal amount,
            final Cause cause
    ) {
        return null;
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(final Set<Context> contexts) {
        return null;
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(final Cause cause) {
        return null;
    }

    @Override
    public TransactionResult resetBalance(final Currency currency, final Set<Context> contexts) {
        return null;
    }

    @Override
    public TransactionResult resetBalance(final Currency currency, final Cause cause) {
        return null;
    }

    @Override
    public TransactionResult deposit(
            final Currency currency,
            final BigDecimal amount,
            final Set<Context> contexts
    ) {
        return null;
    }

    @Override
    public TransactionResult deposit(
            final Currency currency,
            final BigDecimal amount,
            final Cause cause
    ) {
        return null;
    }

    @Override
    public TransactionResult withdraw(
            final Currency currency,
            final BigDecimal amount,
            final Set<Context> contexts
    ) {
        return null;
    }

    @Override
    public TransactionResult withdraw(
            final Currency currency,
            final BigDecimal amount,
            final Cause cause
    ) {
        return null;
    }

    @Override
    public TransferResult transfer(
            final Account to,
            final Currency currency,
            final BigDecimal amount,
            final Set<Context> contexts
    ) {
        return null;
    }

    @Override
    public TransferResult transfer(
            final Account to,
            final Currency currency,
            final BigDecimal amount,
            final Cause cause
    ) {
        return null;
    }

    @Override
    public String identifier() {
        return null;
    }

}
