/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.math.BigDecimal;
import java.util.UUID;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;

/**
 * Represents an enum, describing all the permissions a {@link PlayerAccount player account} may
 * have on a {@link NonPlayerAccount non player account}.
 *
 * @author MrNemo64
 * @see Account
 * @since v1.0.0
 */
public enum AccountPermission {

    /**
     * Allows a player to see the balance of the {@link Account}.
     *
     * @see Account#retrieveBalance(Currency)
     * @since v1.0.0
     */
    BALANCE,

    /**
     * Allows a player to withdraw from the {@link Account}.
     *
     * @see Account#withdrawBalance(BigDecimal, EconomyTransactionInitiator, Currency)
     * @since v1.0.0
     */
    WITHDRAW,

    /**
     * Allows a player to deposit on the {@link Account}.
     *
     * @see Account#depositBalance(BigDecimal, EconomyTransactionInitiator, Currency)
     * @since v1.0.0
     */
    DEPOSIT,

    /**
     * Allows a player to modify the permissions of other players on a {@link Account}
     *
     * @see NonPlayerAccount#setPermissions(UUID, TriState, AccountPermission...)
     * @since v1.0.0
     */
    MODIFY_PERMISSIONS
}
