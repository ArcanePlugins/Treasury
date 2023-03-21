/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransferResult;

public class UniqueAccountImpl implements UniqueAccount {

    private final PlayerAccount delegatePlayerAccount;

    public UniqueAccountImpl(PlayerAccount delegatePLayerAccount) {
        this.delegatePlayerAccount = delegatePLayerAccount;
    }

    @Override
    public Component displayName() {
        return
    }

    @Override
    public BigDecimal defaultBalance(final Currency currency) {
        return null;
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

    @Override
    public UUID uniqueId() {
        return null;
    }

}
