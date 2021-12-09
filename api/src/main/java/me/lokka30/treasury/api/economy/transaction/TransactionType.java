/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.transaction;

/**
 * Denotes which type of transaction occured in a {@link Transaction}.
 *
 * @author lokka30
 * @see Transaction
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public enum TransactionType {

    /**
     * The Account's new balance is greater than their previous balance.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    DEPOSIT,

    /**
     * The Account's new balance is less than their previous balance.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    WITHDRAWAL

}
