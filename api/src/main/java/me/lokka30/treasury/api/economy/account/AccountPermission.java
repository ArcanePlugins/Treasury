/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.math.BigDecimal;
import java.util.UUID;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.common.misc.TriState;

/**
 * Enum that holds the permissions of an {@link Account} that is shared among multiple players.
 *
 * @author MrNemo64
 * @see Account
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public enum AccountPermission {

    /**
     * Allows a player to see the balance of the {@link Account}.
     *
     * @see Account#retrieveBalance(Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    BALANCE,

    /**
     * Allows a player to withdraw from the {@link Account}.
     *
     * @see Account#withdrawBalance(BigDecimal, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    WITHDRAW,

    /**
     * Allows a player to deposit on the {@link Account}.
     *
     * @see Account#depositBalance(BigDecimal, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    DEPOSIT,

    /**
     * Allows a player to modify the permissions of other players on a {@link Account}
     *
     * @see NonPlayerAccount#setPermission(UUID, TriState, EconomySubscriber, AccountPermission...)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    MODIFY_PERMISSIONS
}
