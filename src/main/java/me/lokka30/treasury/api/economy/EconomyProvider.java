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
import me.lokka30.treasury.api.economy.response.EconomyResponse;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

    @NotNull
    CompletableFuture<EconomyResponse<Boolean>> hasPlayerAccount(@NotNull UUID accountId);

    @Nullable
    CompletableFuture<EconomyResponse<PlayerAccount>>  getPlayerAccount(@NotNull UUID accountId);

    @NotNull
    CompletableFuture<EconomyResponse<PlayerAccount>> createPlayerAccount(@NotNull UUID accountId);

    @NotNull
    CompletableFuture<EconomyResponse<Collection<? extends UUID>>> getPlayerAccountIds();

    @NotNull
    CompletableFuture<EconomyResponse<Boolean>> hasBankAccount(@NotNull UUID accountId);

    @Nullable
    CompletableFuture<EconomyResponse<BankAccount>> getBankAccount(@NotNull UUID accountId);

    @NotNull
    CompletableFuture<EconomyResponse<BankAccount>> createBankAccount(@NotNull UUID accountId);

    @Nullable
    CompletableFuture<EconomyResponse<Collection<? extends UUID>>> getBankAccountIds();

    @NotNull
    CompletableFuture<EconomyResponse<Collection<? extends UUID>>> getCurrencyIds();

    @NotNull
    CompletableFuture<EconomyResponse<Collection<? extends String>>> getCurrencyNames();

    @Nullable
    Currency getCurrency(UUID currencyId);

    @Nullable
    Currency getCurrency(String currencyName);

    @NotNull
    default Currency getPrimaryCurrency() {
        return Objects.requireNonNull(getCurrency(getPrimaryCurrencyId()));
    }

    @NotNull
    UUID getPrimaryCurrencyId();

}
