/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.events;

import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event, called when a {@link NonPlayerAccount} does a {@link EconomyTransaction}
 *
 * @author lokka30, MrIvanPlays
 * @since v1.1.0
 */
public class NonPlayerAccountTransactionEvent extends AccountTransactionEvent {

    public NonPlayerAccountTransactionEvent(
            @NotNull EconomyTransaction economyTransaction,
            @NotNull NonPlayerAccount account
    ) {
        super(economyTransaction, account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull NonPlayerAccount getAccount() {
        return (NonPlayerAccount) super.getAccount();
    }

}
