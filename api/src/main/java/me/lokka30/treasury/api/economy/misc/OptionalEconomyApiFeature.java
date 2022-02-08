/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.misc;

/**
 * Economy Providers may support these specific features at their own option.
 *
 * @author lokka30
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public enum OptionalEconomyApiFeature {

    /**
     * Represents that the Economy Provider calls Treasury's
     * AccountTransactionEvents when transactions occur.
     *
     * <p>Economy Providers which do not operate on the Bukkit
     * platform should not support this feature.
     *
     * @see me.lokka30.treasury.api.economy.event
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     * @deprecated use {@link #TRANSACTION_EVENTS}
     */
    @Deprecated
    BUKKIT_TRANSACTION_EVENTS,

    /**
     * Represents that the Economy Provider calls Treasury's
     * AccountTransaction events when transactions occur.
     *
     * @see me.lokka30.treasury.api.economy.events
     * @since {@link EconomyAPIVersion#V1_1 v1.1}
     */
    TRANSACTION_EVENTS,

    /**
     * Represents that the Economy Provider supports managing
     * balances of all accounts with negative (below-zero) balances.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    NEGATIVE_BALANCES

}
