/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import com.mrivanplays.process.Process;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.common.response.TreasuryException;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;

class PlayerAccountMigrationProcess extends Process {

    protected final Cause<?> cause;
    protected final String accountId;
    protected final MigrationData migration;
    protected Account fromAccount, toAccount;

    public PlayerAccountMigrationProcess(
            @NotNull Cause<?> cause,
            @NotNull String accountId,
            @NotNull MigrationData migration
    ) {
        this.cause = cause;
        this.accountId = accountId;
        this.migration = migration;
    }

    @Override
    protected void run() throws Throwable {
        this.migration.debug(this::getInitLog);

        // Retrieve accounts
        this.fromAccount = this.requestOrCreateAccount(this.migration.from(),
                this.accountId
        ).get();
        this.toAccount = this.requestOrCreateAccount(this.migration.to(),
                this.accountId
        ).get();

        // retrieve currencies
        Collection<String> heldCurrencies = fromAccount
                .retrieveHeldCurrencies()
                .get();

        for (String id : heldCurrencies) {
            // find currency in the new provider
            Optional<Currency> currencyOpt = this.migration.to().findCurrency(id);
            // find old currency
            Optional<Currency> oldCurrencyOpt = this.migration.from().findCurrency(id);
            Currency currency;
            Currency oldCurrency;
            if (currencyOpt.isPresent() && oldCurrencyOpt.isPresent()) {
                currency = currencyOpt.get();
                oldCurrency = oldCurrencyOpt.get();
            } else {
                this.migration.debug(() -> "Currency with ID '&b" + id + "&7' will not be migrated for account '" + this.accountId + "'.");
                Collection<String> currencies = this.migration
                        .nonMigratedCurrencies()
                        .get(this.accountId);
                if (!currencies.contains(id)) {
                    this.migration.nonMigratedCurrencies().put(this.accountId, id);
                }
                continue;
            }

            try {
                // get balance
                BigDecimal balance = fromAccount.retrieveBalance(oldCurrency).get();
                if (balance == null || balance.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                if (!currency.isPrimary() && oldCurrency.isPrimary() && !this.migration
                        .migratedCurrencies()
                        .contains(currency.getIdentifier())) {
                    // in case the new provider's primary currency is different, make sure to convert
                    // the old currency value to the new one
                    BigDecimal newBalance = oldCurrency.to(currency, balance);
                    if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    balance = newBalance;
                }

                if (balance.compareTo(BigDecimal.ZERO) < 0) {
                    toAccount
                            .withdrawBalance(balance, this.cause, currency)
                            .get();
                } else {
                    toAccount
                            .depositBalance(balance, this.cause, currency)
                            .get();
                }
            } catch (TreasuryException e) {
                this.migration.debug(() -> this.getErrorLog(e));
                this.migration.debug(() -> String.format(
                        "Failed to recover from an issue transferring %s from %s, currency will not be migrated!",
                        currency.getIdentifier(),
                        this.accountId
                ));
                Collection<String> currencies = this.migration
                        .nonMigratedCurrencies()
                        .get(this.accountId);
                if (!currencies.contains(id)) {
                    this.migration.nonMigratedCurrencies().put(this.accountId, id);
                }
            }
        }

        if (!nonPlayer()) {
            this.migration.playerAccountsProcessed().incrementAndGet();
        }
    }

    @NotNull String getInitLog() {
        return "Migrating player account of UUID '&b" + this.accountId + "&7'.";
    }

    @NotNull String getErrorLog(@NotNull TreasuryException error) {
        return "Error migrating account of player UUID '&b" + this.accountId + "&7': &b" + error.getMessage();
    }

    @NotNull CompletableFuture<Account> requestOrCreateAccount(
            @NotNull EconomyProvider provider, @NotNull String identifier
    ) {
        UUID uuid;
        try {
            uuid = UUID.fromString(identifier);
        } catch (IllegalArgumentException ignored) {
            return FutureHelper.failedFuture(new TreasuryException("Invalid UUID of player " + "account"));
        }
        return provider.accountAccessor().player().withUniqueId(uuid).genericGet();
    }

    boolean nonPlayer() {
        return false;
    }

}
