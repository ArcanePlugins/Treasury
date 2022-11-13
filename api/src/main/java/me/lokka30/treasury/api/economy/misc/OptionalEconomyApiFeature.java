/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.misc;

/**
 * Economy Providers may support these specific features at their own option.
 *
 * @author lokka30
 * @since v1.0.0
 */
public enum OptionalEconomyApiFeature {

    /**
     * Represents that the Economy Provider calls Treasury's
     * AccountTransactionEvents when transactions occur, however,
     * the economy provider is using Treasury's deprecated Bukkit
     * events (at {@code me.lokka30.treasury.api.economy.event}).
     *
     * <p>Economy Providers which do not operate on the Bukkit
     * platform should not support this feature.
     *
     * @since v1.0.0
     * @deprecated use {@link #TRANSACTION_EVENTS} instead
     */
    @Deprecated
    BUKKIT_TRANSACTION_EVENTS,

    /**
     * Represents that the Economy Provider calls Treasury's
     * AccountTransaction events when transactions occur.
     *
     * @see me.lokka30.treasury.api.economy.events
     * @since v1.1.0
     */
    TRANSACTION_EVENTS,

    /**
     * Represents that the Economy Provider supports managing
     * balances of all accounts with negative (below-zero) balances.
     *
     * @since v1.0.0
     */
    NEGATIVE_BALANCES

}
