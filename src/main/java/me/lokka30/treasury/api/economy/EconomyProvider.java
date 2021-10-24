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

import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

/**
 * @author lokka30
 * @since v1.0.0
 * Economy Providers (plugins facilitating the economy data) create
 * a class which implements this interface and should then become a
 * `RegisteredServiceProvider<EconomyProvider>`.
 */
@SuppressWarnings({"unused", "RedundantThrows", "UnusedReturnValue"})
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
    @NotNull
    EconomyAPIVersion getSupportedAPIVersion();

    /**
     * @author lokka30, NoahvdAa
     * @since v1.0.0
     * This method should be asserted before any Bank Account
     * methods are accessed through the Treasury API.
     * @return whether the economy provider supports bank accounts.
     */
    default boolean hasBankAccountSupport() { return false; }

    /**
     * @author lokka30, NoahvdAa
     * @since v1.0.0
     * This method returns whether the economy provider calls Treasury's
     * in-built transaction events (see {@link me.lokka30.treasury.api.economy.event.AccountTransactionEvent}).
     * This method should be asserted before a plugin tries to listen to
     * Treasury's events, as otherwise the economy provider installed
     * may not have transaction event support, and thus the events will
     * never be called.
     * @return whether the economy provider calls Treasury's in-built transaction events.
     */
    default boolean hasTransactionEventSupport() { return false; }

    /**
     * @author lokka30, NoahvdAa
     * @since v1.0.0
     * Some economy providers support negative / below-zero balances.
     * This method allows economy consumers to check if
     * the provider supports negative balances or not.
     * @return whether the economy provider supports negative / below-zero balances.
     */
    default boolean hasNegativeBalanceSupport() { return false; }

    void hasPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription);

    void requestPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription);

    void createPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription);

    void requestPlayerAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    void hasBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription);

    void requestBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription);

    void createBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription);

    void requestBankAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    void requestCurrencyIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    void requestCurrencyNames(@NotNull EconomySubscriber<Collection<String>> subscription);

    void requestCurrency(UUID currencyId, @NotNull EconomySubscriber<Currency> subscription);

    void requestCurrency(String currencyName, @NotNull EconomySubscriber<Currency> subscription);

    @NotNull
    Currency getPrimaryCurrency();

    @NotNull
    default UUID getPrimaryCurrencyId() {
        return getPrimaryCurrency().getCurrencyId();
    }

}
