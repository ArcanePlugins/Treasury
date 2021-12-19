package me.lokka30.treasury.api.economy.response;

import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;

public enum EconomyFailureReason implements FailureReason {

    /**
     * Use this constant if the method can't be run in any capacity
     * as the economy provider does not provide support for the method.
     * It is paramount that plugins ensure that economy providers support
     * certain methods (e.g. bank accounts) before attempting to access them.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    FEATURE_NOT_SUPPORTED {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "That feature is currently not supported by this Economy Provider.";
        }
    },

    /**
     * A constant represeting failure due to economies being in the middle
     * of migrating.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    MIGRATION {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "That feature is currently not available during migration!";
        }
    },

    /**
     * A constant representing failure due to request cancellation.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    REQUEST_CANCELLED {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "The pending action requested was cancelled.";
        }
    },

    /* Accounts */

    /**
     * A constant representing failure due to the inability to locate an
     * {@link me.lokka30.treasury.api.economy.account.Account Account}.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    ACCOUNT_NOT_FOUND {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "The account you attempted to perform that action on was unable to be located.";
        }
    },

    /**
     * A constant representing failure due to
     * {@link me.lokka30.treasury.api.economy.account.Account Account}
     * deletion being unsupported.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    ACCOUNT_DELETION_NOT_SUPPORTED {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "Account deletion is not supported.";
        }
    },

    /**
     * A constant representing failure due to an
     * {@link me.lokka30.treasury.api.economy.account.Account Account}
     * already existing.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    ACCOUNT_ALREADY_EXISTS {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "An account with that identifier already exists!";
        }
    },

    /* GenericAccounts */

    /**
     * A constant representing failure due to a user already being a member of a
     * {@link me.lokka30.treasury.api.economy.account.BankAccount BankAccount}.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    ALREADY_MEMBER_OF_ACCOUNT {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "That feature is currently not supported by this Economy Provider.";
        }
    },

    /**
     * A constant representing failure due to a user already being an owner of a
     * {@link me.lokka30.treasury.api.economy.account.BankAccount BankAccount}.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    ALREADY_OWNER_OF_ACCOUNT {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "The specified user is already a member of the account mentioned.";
        }
    },

    /**
     * A constant representing failure due to a user already not being a member of a
     * {@link me.lokka30.treasury.api.economy.account.BankAccount BankAccount}.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    ALREADY_NOT_MEMBER_OF_ACCOUNT {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "The specified user is already not a member of the account mentioned.";
        }
    },

    /**
     * A constant representing failure due to a user already not being an owner of a
     * {@link me.lokka30.treasury.api.economy.account.BankAccount BankAccount}.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    ALREADY_NOT_OWNER_OF_ACCOUNT {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "The specified user is already not an owner of the account mentioned.";
        }
    },

    /* Balances */

    /**
     * A constant representing failure due to an overdraft when
     * negative balances are not supported.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    NEGATIVE_BALANCES_NOT_SUPPORTED {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "This economy provider does not support negative balances!";
        }
    },

    /**
     * A constant representing failure due to a negative amount being
     * provided to a method that only accepts positive numbers.
     *

     * @see me.lokka30.treasury.api.economy.account.Account#withdrawBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @see me.lokka30.treasury.api.economy.account.Account#depositBalance(double, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    NEGATIVE_AMOUNT_SPECIFIED {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "This economy provider does not support negative amounts!";
        }
    },

    /* Currencies */

    /**
     * A constant representing failure due to the inability to locate a
     * {@link me.lokka30.treasury.api.economy.currency.Currency Currency}.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    CURRENCY_NOT_FOUND {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "The specified currency was unable to be located.";
        }
    },

    /**
     * A constant representing failure due to a null parameter
     * being specified when a null parameter was not expected.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    NULL_PARAMETER {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "Action failed because a provided parameter was null.";
        }
    },

    /**
     * Use this constant if the method resulted in a complete failure,
     * AND no other constant in this enum is applicable to the issue
     * that occurred. In this case, use this constant, and please
     * submit a pull request or issue so that a future Treasury version
     * can accommodate for this issue.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    OTHER_FAILURE {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "Unexpected failure occurred.";
        }
    }
}
