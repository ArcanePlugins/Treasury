/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy;

import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

/**
 * Implementors providing and managing economy data create a class
 * which implements this interface to be registered in
 * the specific platform they're implementing it for.
 *
 * @author lokka30
 * @since v1.0.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface EconomyProvider {

    /**
     * Get the version of the Treasury API the {@code EconomyProvider} is based on.
     *
     * <p>Please note that the Treasury API version is not the same as the Spigot API version!
     *
     * @author lokka30
     * @return the API version
     * @since v1.0.0
     */
    @NotNull
    EconomyAPIVersion getSupportedAPIVersion();

    /**
     * Check whether the economy provides {@link BankAccount} implementations.
     *
     * <p>This should be checked before using bank-related methods.
     *
     * @author lokka30, NoahvdAa
     * @return whether the economy supports bank accounts
     * @since v1.0.0
     */
    default boolean hasBankAccountSupport() { return false; }

    /**
     * Check whether the {@code EconomyProvider} calls Treasury's in-built
     * transaction events.
     *
     * <p>This should be checked before relying on Treasury's events as
     * the {@code EconomyProvider} may not have transaction event support,
     * and thus the events will never be called.
     *
     * @author lokka30, NoahvdAa
     * @return whether the economy calls Treasury's transaction events
     * @see me.lokka30.treasury.api.economy.event
     * @see me.lokka30.treasury.api.economy.event.AccountTransactionEvent
     * @since v1.0.0
     */
    default boolean hasTransactionEventSupport() { return false; }

    /**
     * Check whether the {@code EconomyProvider} supports negative
     * or below-zero balances.
     *
     * @author lokka30, NoahvdAa
     * @return whether the economy supports negative balances
     * @since v1.0.0
     */
    default boolean hasNegativeBalanceSupport() { return false; }

    /**
     * Request whether a user has an associated {@link PlayerAccount}.
     *
     * @param accountId the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void hasPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Request an existing {@link PlayerAccount} for a user.
     *
     * @param accountId the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrievePlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription);

    /**
     * Request the creation of a {@link PlayerAccount} for a user.
     *
     * @param accountId the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void createPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription);

    /**
     * Request all {@link UUID UUIDs} with associated {@link PlayerAccount PlayerAccounts}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrievePlayerAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request whether a {@link UUID} has an associated {@link BankAccount}.
     *
     * @param accountId the {@code UUID} of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void hasBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Request an existing {@link BankAccount} for a {@link UUID}.
     *
     * @param accountId the {@code UUID} of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrieveBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription);

    /**
     * Request the creation of a {@link BankAccount} for a {@link UUID}.
     *
     * @param accountId the {@code UUID} of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void createBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription);

    /**
     * Request all {@link UUID UUIDs} with associated {@link BankAccount BankAccounts}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrieveBankAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request all {@link UUID UUIDs} for valid {@link Currency Currencies}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrieveCurrencyIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request all names for valid {@link Currency Currencies}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrieveCurrencyNames(@NotNull EconomySubscriber<Collection<String>> subscription);

    /**
     * Request a {@link Currency} by {@link UUID}.
     *
     * @param currencyId the {@code UUID} identifying the {@code Currency}
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrieveCurrency(@NotNull UUID currencyId, @NotNull EconomySubscriber<Currency> subscription);

    /**
     * Request a {@link Currency} by name.
     *
     * @param currencyName the name of the {@code Currency}
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @since v1.0.0
     */
    void retrieveCurrency(@NotNull String currencyName, @NotNull EconomySubscriber<Currency> subscription);

    /**
     * Get the primary or main {@link Currency} of the economy.
     *
     * @return the primary currency
     * @since v1.0.0
     */
    @NotNull
    Currency getPrimaryCurrency();

    /**
     * Get the {@link UUID} of the primary or main {@link Currency} of the economy.
     *
     * @return the {@code UUID} identifying the primary currency
     * @since v1.0.0
     */
    @NotNull
    default UUID getPrimaryCurrencyId() {
        return getPrimaryCurrency().getCurrencyId();
    }

}
