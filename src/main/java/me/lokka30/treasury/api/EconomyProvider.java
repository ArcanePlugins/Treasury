/*
 * Copyright (c) 2021 lokka30.
 * This code is part of Treasury, an Economy API for Minecraft servers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You have received a copy of the GNU Affero General Public License
 * with this program - please see the LICENSE.md file. Alternatively,
 * please visit the <https://www.gnu.org/licenses/> website.
 *
 * Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 */

package me.lokka30.treasury.api;

import com.sun.source.util.Plugin;
import me.lokka30.treasury.api.account.BankAccount;
import me.lokka30.treasury.api.account.NonPlayerAccount;
import me.lokka30.treasury.api.account.PlayerAccount;
import me.lokka30.treasury.api.currency.Currency;
import me.lokka30.treasury.api.exception.AccountAlreadyExistsException;
import me.lokka30.treasury.api.exception.InvalidCurrencyException;
import me.lokka30.treasury.api.exception.UnsupportedEconomyFeatureException;
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
    @NotNull Plugin getProvider();

    /**
     * @author lokka30
     * @since v1.0.0
     * @return which API version of Treasury the Provider is based on.
     */
    short getSupportedAPIVersion();

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

    @NotNull PlayerAccount getPlayerAccount(@NotNull UUID accountId);

    void createPlayerAccount(@NotNull UUID accountId) throws AccountAlreadyExistsException;

    @NotNull List<UUID> getPlayerAccountIds();

    /**
     * @author lokka30
     * @since v1.0.0
     * @throws UnsupportedEconomyFeatureException if the Provider does not support this method, as indicated by `EconomyProvider#hasNonPlayerAccountSupport`.
     * Check if the non-player account exists under the specified UUID.
     * @param accountId uuid of the non-player account to check.
     * @return whether the non-player account exists.
     */
    boolean hasNonPlayerAccount(@NotNull UUID accountId) throws UnsupportedEconomyFeatureException;

    @NotNull NonPlayerAccount getNonPlayerAccount(UUID accountId) throws UnsupportedEconomyFeatureException;

    void createNonPlayerAccount(@NotNull UUID accountId) throws UnsupportedEconomyFeatureException, AccountAlreadyExistsException;

    @NotNull List<UUID> getNonPlayerAccountIds() throws UnsupportedEconomyFeatureException;

    boolean hasBankAccount(@NotNull UUID accountId) throws UnsupportedEconomyFeatureException;

    @NotNull BankAccount getBankAccount(@NotNull UUID accountId) throws UnsupportedEconomyFeatureException;

    void createBankAccount(@NotNull UUID accountId, @NotNull UUID owningPlayerId) throws UnsupportedEconomyFeatureException, AccountAlreadyExistsException;

    @NotNull List<UUID> getBankAccountIds() throws UnsupportedEconomyFeatureException;

    @NotNull List<UUID> getCurrencyIds();

    @NotNull List<String> getCurrencyNames();

    @NotNull Currency getCurrency(UUID currencyId) throws InvalidCurrencyException;

    @NotNull Currency getCurrency(String currencyName) throws InvalidCurrencyException;

    @NotNull default Currency getPrimaryCurrency() throws InvalidCurrencyException {
        return getCurrency(getPrimaryCurrencyId());
    }

    @NotNull UUID getPrimaryCurrencyId() throws InvalidCurrencyException;

}
