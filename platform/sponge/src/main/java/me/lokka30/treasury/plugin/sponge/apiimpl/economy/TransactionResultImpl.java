/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.math.BigDecimal;
import java.util.Set;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;

public class TransactionResultImpl implements TransactionResult {

    private final Account account;
    private final Currency currency;
    private final BigDecimal amount;
    private final TransactionType type;
    private final ResultType result;
    private final Set<Context> contexts;

    public TransactionResultImpl(
            final Account account,
            final Currency currency,
            final BigDecimal amount,
            final TransactionType type,
            final ResultType result,
            final Set<Context> contexts
    ) {
        this.account = account;
        this.currency = currency;
        this.amount = amount;
        this.type = type;
        this.result = result;
        this.contexts = contexts;
    }

    @Override
    public Account account() {
        return this.account;
    }

    @Override
    public Currency currency() {
        return this.currency;
    }

    @Override
    public BigDecimal amount() {
        return this.amount;
    }

    @Override
    public Set<Context> contexts() {
        return this.contexts;
    }

    @Override
    public ResultType result() {
        return this.result;
    }

    @Override
    public TransactionType type() {
        return this.type;
    }

}
