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
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransferResult;

public class TransferResultImpl extends TransactionResultImpl implements TransferResult {

    private final Account accountTo;

    public TransferResultImpl(
            final Account account,
            final Account accountTo,
            final Currency currency,
            final BigDecimal amount,
            final TransactionType type,
            final ResultType result,
            final Set<Context> contexts
    ) {
        super(account, currency, amount, type, result, contexts);
        this.accountTo = accountTo;
    }

    @Override
    public Account accountTo() {
        return this.accountTo;
    }

}
