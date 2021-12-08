/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.migrate;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.response.FailureReason;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

/**
 * A dummy {@link EconomyProvider} used to prevent transactions with
 *
 * @since v1.0.0
 */
class MigrationEconomy implements EconomyProvider {

    private final @NotNull Currency currency;
    private final @NotNull EconomyException migrationException;

    MigrationEconomy() {
        this.currency = Currency.of(
                null,
                0,
                1,
                (amt, $) -> String.valueOf(amt),
                "MigrationMoney"
        );
        this.migrationException = new EconomyException(FailureReason.MIGRATION, "Economy unavailable during migration process.");
    }

    @Override
    public @NotNull EconomyAPIVersion getSupportedAPIVersion() {
        return EconomyAPIVersion.getCurrentAPIVersion();
    }

    @Override
    public void hasPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrievePlayerAccount(
            @NotNull UUID accountId,
            @NotNull EconomySubscriber<PlayerAccount> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void createPlayerAccount(
            @NotNull UUID accountId,
            @NotNull EconomySubscriber<PlayerAccount> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrievePlayerAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void hasBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void createBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveBankAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveCurrencyIds(@NotNull EconomySubscriber<Collection<UUID>> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveCurrencyNames(@NotNull EconomySubscriber<Collection<String>> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveCurrency(@NotNull UUID currencyId, @NotNull EconomySubscriber<Currency> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveCurrency(@NotNull String currencyName, @NotNull EconomySubscriber<Currency> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public @NotNull Currency getPrimaryCurrency() {
        return currency;
    }

}
