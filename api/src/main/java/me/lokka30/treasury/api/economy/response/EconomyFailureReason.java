package me.lokka30.treasury.api.economy.response;

import java.math.BigDecimal;
import java.util.UUID;
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.misc.TriState;
import org.jetbrains.annotations.NotNull;

public enum EconomyFailureReason implements FailureReason {

    /**
     * A constant representing failure due to a
     * {@link me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature}
     * being attempted to be used, but it is not supported by the economy provider.
     * To avoid this failure, ensure the associated constant in
     * {@link me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature} is
     * inside the set obtained from
     * {@link EconomyProvider#getSupportedOptionalEconomyApiFeatures()}.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    FEATURE_NOT_SUPPORTED {
        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "The feature is not supported by this Economy Provider.";
        }
    },

    /**
     * A constant representing failure due to an operation being cancelled due to economy
     * migration being in progress.
     * To avoid this failure, the server owner should suspend any activities whilst migrating
     * between economy providers.
     *
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    MIGRATION {
        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "That feature is currently not available during migration.";
        }
    },

    // TODO document further. check what this constant would be used for.
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

    /**
     * A constant representing failure due to the inability to locate an
     * {@link me.lokka30.treasury.api.economy.account.Account Account}.
     * To avoid this failure, check if the account exists before attempting to
     * interact with it.
     *
     * @see EconomyProvider#hasAccount(String, EconomySubscriber)
     * @see EconomyProvider#hasPlayerAccount(UUID, EconomySubscriber)
     * @see EconomyProvider#retrieveAccount(String, EconomySubscriber)
     * @see EconomyProvider#retrievePlayerAccount(UUID, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    ACCOUNT_NOT_FOUND {
        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public String getDescription() {
            return "The account you attempted to perform that action on was unable to be located.";
        }
    },

    /**
     * A constant representing failure due to an
     * {@link me.lokka30.treasury.api.economy.account.Account Account}
     * already existing, whilst attempting to create one with the same identifier.
     * To avoid this failure, check if an account exists before attempting to create
     * one that uses the same identifier.
     *
     * @see EconomyProvider#hasAccount(String, EconomySubscriber)
     * @see EconomyProvider#hasPlayerAccount(UUID, EconomySubscriber)
     * @see EconomyProvider#createAccount(String, EconomySubscriber)
     * @see EconomyProvider#createAccount(String, String, EconomySubscriber)
     * @see EconomyProvider#createPlayerAccount(UUID, EconomySubscriber)
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

    /**
     * A constant representing failure whenever the default implementation of
     * {@link PlayerAccount#setPermission(UUID, TriState, EconomySubscriber, AccountPermission...)}
     * has been called.
     * To avoid this failure, do not try to set permissions on player accounts.
     *
     * @see PlayerAccount#setPermission(UUID, TriState, EconomySubscriber, AccountPermission...)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    PLAYER_ACCOUNT_PERMISSION_MODIFICATION_NOT_SUPPORTED {
        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "Cannot modify the permissions of a player account!";
        }
    },

    /**
     * A constant representing failure due to an overdraft when
     * negative balances are not supported.
     * To avoid this failure, check if the economy provider supports negative balances by
     * checking if the {@code NEGATIVE_BALANCES} constant exists in the collection from
     * {@link EconomyProvider#getSupportedOptionalEconomyApiFeatures()}.
     *
     * @see EconomyProvider#getSupportedOptionalEconomyApiFeatures()
     * @see me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature#NEGATIVE_BALANCES
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
     * Note: This does not apply to every method in the Economy API!
     * To avoid this failure, ensure such parameters only receive positive numbers.
     *
     * @see me.lokka30.treasury.api.economy.account.Account#withdrawBalance(BigDecimal, EconomyTransactionInitiator, Currency, EconomySubscriber)
     * @see me.lokka30.treasury.api.economy.account.Account#depositBalance(BigDecimal, EconomyTransactionInitiator, Currency, EconomySubscriber)
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

    /**
     * A constant representing failure due to the inability to locate a {@link Currency Currency}.
     * To avoid this failure, check if a currency exists before attempting to use it.
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
     * A constant representing failure due to the {@link Currency} already being registered by the
     * economy provider.
     * To avoid this failure, check if a currency exists before attempting to register it.
     *
     * @see me.lokka30.treasury.api.economy.EconomyProvider#registerCurrency(Currency, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    CURRENCY_ALREADY_REGISTERED {
        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "The specified currency is already registered in the economy provider.";
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
     * A constant representing failure due to a String not
     * following an expected format/pattern when attempting
     * to parse it into a number type such as {@link BigDecimal}.
     *
     * @see Currency#parse(String, EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    NUMBER_PARSING_ERROR {
        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDescription() {
            return "Action failed because a String was unable to be converted into a number due to an incompatible format.";
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
