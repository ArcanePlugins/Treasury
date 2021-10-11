/*
 * Copyright (c) 2021-2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.api.economy;

import com.sun.source.util.Plugin;
import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.exception.AccountAlreadyExistsException;
import me.lokka30.treasury.api.economy.exception.InvalidCurrencyException;
import me.lokka30.treasury.api.economy.exception.UnsupportedEconomyFeatureException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * @author lokka30
 * @since v1.0.0
 * Economy Providers (plugins facilitating the economy data) create
 * a class which implements this interface and should then become a
 * `RegisteredServiceProvider<EconomyProvider>`.
 */
@SuppressWarnings({"unused", "RedundantThrows"})
public interface EconomyProvider {

    /**
     * @author lokka30
     * @since v1.0.0
     * @return the Plugin facilitating the economy - the 'Economy Provider'.
     */
    @NotNull
    Plugin getProvider();

    /**
     * @author lokka30
     * @since v1.0.0
     * @return which API version of Treasury the Provider is based on.
     */
    EconomyAPIVersion getSupportedAPIVersion();

    /**
     * @author lokka30
     * @since v1.0.0
     * This method should be asserted before any Non-Player Account
     * methods are accessed through the Treasury API.
     * @return whether the Provider supports non-player accounts.
     */
    boolean hasNonPlayerAccountSupport();

    /**
     * @author lokka30
     * @since v1.0.0
     * This method should be asserted before any Bank Account
     * methods are accessed through the Treasury API.
     * @return whether the Provider supports bank accounts.
     */
    boolean hasBankAccountSupport();

    /**
     * @author lokka30
     * @since v1.0.0
     * This method should be checked to see if the Provider
     * fully supports per-world balances. If the Provider does
     * not use per-world balances then it is guaranteed safe to
     * specify empty Strings for world names in methods such as
     * 'getBalance'.
     * @return whether the Provider supports per-world balances.
     */
    boolean hasPerWorldBalanceSupport();

    boolean hasTransactionEventSupport();

    boolean hasPlayerAccount(@NotNull UUID accountId);

    @NotNull
    PlayerAccount getPlayerAccount(@NotNull UUID accountId);

    void createPlayerAccount(@NotNull UUID accountId) throws AccountAlreadyExistsException;

    @NotNull
    List<UUID> getPlayerAccountIds();

    /**
     * @author lokka30
     * @since v1.0.0
     * @throws UnsupportedEconomyFeatureException if the Provider does not support this method, as indicated by `EconomyProvider#hasNonPlayerAccountSupport`.
     * Check if the non-player account exists under the specified UUID.
     * @param accountId uuid of the non-player account to check.
     * @return whether the non-player account exists.
     */
    boolean hasNonPlayerAccount(@NotNull UUID accountId) throws UnsupportedEconomyFeatureException;

    @NotNull
    NonPlayerAccount getNonPlayerAccount(@NotNull UUID accountId) throws UnsupportedEconomyFeatureException;

    void createNonPlayerAccount(@NotNull UUID accountId) throws UnsupportedEconomyFeatureException, AccountAlreadyExistsException;

    @NotNull
    List<UUID> getNonPlayerAccountIds() throws UnsupportedEconomyFeatureException;

    boolean hasBankAccount(@NotNull UUID accountId) throws UnsupportedEconomyFeatureException;

    @NotNull
    BankAccount getBankAccount(@NotNull UUID accountId) throws UnsupportedEconomyFeatureException;

    void createBankAccount(@NotNull UUID accountId, @NotNull UUID owningPlayerId) throws UnsupportedEconomyFeatureException, AccountAlreadyExistsException;

    @NotNull
    List<UUID> getBankAccountIds() throws UnsupportedEconomyFeatureException;

    @NotNull
    List<UUID> getCurrencyIds();

    @NotNull
    List<String> getCurrencyNames();

    @NotNull
    Currency getCurrency(UUID currencyId) throws InvalidCurrencyException;

    @NotNull
    Currency getCurrency(String currencyName) throws InvalidCurrencyException;

    @NotNull
    default Currency getPrimaryCurrency() throws InvalidCurrencyException {
        return getCurrency(getPrimaryCurrencyId());
    }

    @NotNull
    UUID getPrimaryCurrencyId() throws InvalidCurrencyException;

}
