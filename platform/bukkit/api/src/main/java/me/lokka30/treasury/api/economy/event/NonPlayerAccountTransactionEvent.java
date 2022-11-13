/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event, called when a {@link NonPlayerAccount} does a {@link EconomyTransaction}
 *
 * @author lokka30
 * @since v1.0.0
 * @deprecated use {@link me.lokka30.treasury.api.economy.events.NonPlayerAccountTransactionEvent}
 */
@Deprecated
public class NonPlayerAccountTransactionEvent extends AccountTransactionEvent {

    public NonPlayerAccountTransactionEvent(
            @NotNull EconomyTransaction economyTransaction,
            @NotNull NonPlayerAccount account,
            boolean async
    ) {
        super(economyTransaction, account, async);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull NonPlayerAccount getAccount() {
        return (NonPlayerAccount) super.getAccount();
    }

}
