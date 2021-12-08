/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.transaction;

/**
 * @author lokka30
 * @since v1.0.0
 * @see Transaction
 * Denotes which type of transaction occured in a Transsaction.
 */
public enum TransactionType {

    /**
     * @since v1.0.0
     * The Account's new balance is greater than their previous balance.
     */
    DEPOSIT,

    /**
     * @since v1.0.0
     * The Account's new balance is less than their previous balance.
     */
    WITHDRAWAL

}
