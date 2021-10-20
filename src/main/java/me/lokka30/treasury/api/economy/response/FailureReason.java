package me.lokka30.treasury.api.economy.response;

/**
 * A collection of constants describing common causes for request failures.
 *
 * @since v1.0.0
 */
public enum FailureReason {

    /**
     * Use this constant if the method can't be run in any capacity
     * as the economy provider does not provide support for the method.
     * It is paramount that plugins ensure that economy providers support
     * certain methods (e.g. bank accounts) before attempting to access them.
     *
     * @since v1.0.0
     */
    FEATURE_NOT_SUPPORTED,

    /* Accounts */

    // TODO Javadoc
    ACCOUNT_NOT_FOUND,

    // TODO Javadoc
    ACCOUNT_DELETION_NOT_SUPPORTED,

    /* BankAccounts */

    // TODO Javadoc
    ALREADY_MEMBER_OF_BANK_ACCOUNT,

    // TODO Javadoc
    ALREADY_OWNER_OF_BANK_ACCOUNT,

    // TODO Javadoc
    ALREADY_NOT_MEMBER_OF_BANK_ACCOUNT,

    // TODO Javadoc
    ALREADY_NOT_OWNER_OF_BANK_ACCOUNT,

    /* Balances */

    // TODO Javadoc
    NEGATIVE_BALANCES_NOT_SUPPORTED,

    // TODO Javadoc
    NEGATIVE_AMOUNT_SPECIFIED,

    /* Currencies */

    // TODO Javadoc
    CURRENCY_NOT_FOUND,

    /**
     * Use this constant if the method resulted in a complete failure,
     * AND no other constant in this enum is applicable to the issue
     * that occured. In this case, use this constant, and please
     * submit a pull request or issue so that a future Treasury version
     * can accomodate for this issue.
     *
     * @since v1.0.0
     */
    OTHER_FAILURE

}
