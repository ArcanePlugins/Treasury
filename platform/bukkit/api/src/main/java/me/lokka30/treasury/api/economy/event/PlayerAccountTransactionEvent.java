/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.event;

import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event, called when a {@link PlayerAccount} does a {@link EconomyTransaction}
 *
 * @author lokka30
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 * @deprecated use {@link me.lokka30.treasury.api.economy.events.PlayerAccountTransactionEvent}
 */
@Deprecated
public class PlayerAccountTransactionEvent extends AccountTransactionEvent {

    public PlayerAccountTransactionEvent(
            @NotNull EconomyTransaction economyTransaction,
            @NotNull PlayerAccount account,
            boolean async
    ) {
        super(economyTransaction, account, async);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PlayerAccount getAccount() {
        return (PlayerAccount) super.getAccount();
    }

}
