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
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;

class PlayerAccountMigrationProcess extends Process {

    protected final EconomyTransactionInitiator<?> initiator;
    protected final String accountId;
    protected final MigrationData migration;
    protected Account fromAccount, toAccount;

    public PlayerAccountMigrationProcess(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull String accountId,
            @NotNull MigrationData migration
    ) {
        this.initiator = initiator;
        this.accountId = accountId;
        this.migration = migration;
    }

    @Override
    protected void run() throws Throwable {
        this.migration.debug(this::getInitLog);

        // Retrieve accounts
        Response<Account> fromAccountResp = this.requestOrCreateAccount(this.migration.from(),
                this.accountId
        ).get();
        Response<Account> toAccountResp = this.requestOrCreateAccount(this.migration.to(),
                this.accountId
        ).get();

        if (!handleUnsuccessfulResponse(fromAccountResp) || !handleUnsuccessfulResponse(
                toAccountResp)) {
            return;
        }

        this.fromAccount = fromAccountResp.getResult();
        this.toAccount = toAccountResp.getResult();

        // retrieve currencies
        Response<Collection<String>> heldCurrenciesResp = fromAccount
                .retrieveHeldCurrencies()
                .get();
        if (!handleUnsuccessfulResponse(heldCurrenciesResp)) {
            return;
        }

        for (String id : heldCurrenciesResp.getResult()) {
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

            // get balance
            Response<BigDecimal> balResp = fromAccount.retrieveBalance(oldCurrency).get();
            if (!handleUnsuccessfulResponse(balResp)) {
                continue;
            }

            BigDecimal balance = balResp.getResult();
            if (balance == null || balance.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            if (!currency.isPrimary() && oldCurrency.isPrimary() && !this.migration
                    .migratedCurrencies()
                    .contains(currency.getIdentifier())) {
                // in case the new provider's primary currency is different, make sure to convert
                // the old currency value to the new one
                Response<BigDecimal> balRespNew = oldCurrency.to(currency, balance).get();
                if (!handleUnsuccessfulResponse(balRespNew)) {
                    continue;
                }
                BigDecimal newBalance = balRespNew.getResult();
                if (newBalance == null || newBalance.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                balance = newBalance;
            }

            Response<BigDecimal> transactionResponse;
            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                transactionResponse = toAccount
                        .withdrawBalance(balance, this.initiator, currency)
                        .get();
            } else {
                transactionResponse = toAccount
                        .depositBalance(balance, this.initiator, currency)
                        .get();
            }

            if (transactionResponse == null || !transactionResponse.isSuccessful()) {
                this.migration.debug(() -> this.getErrorLog(transactionResponse.getFailureReason()));
                this.migration.debug(() -> String.format(
                        "Failed to recover from an issue transferring %s from %s, currency will not be migrated!",
                        currency.getDisplayNameSingular(),
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

    boolean handleUnsuccessfulResponse(Response<?> response) {
        if (!response.isSuccessful()) {
            migration.debug(() -> this.getErrorLog(response.getFailureReason()));
            return false;
        }
        return true;
    }

    @NotNull String getInitLog() {
        return "Migrating player account of UUID '&b" + this.accountId + "&7'.";
    }

    @NotNull String getErrorLog(@NotNull FailureReason failureReason) {
        return "Error migrating account of player UUID '&b" + this.accountId + "&7': &b" + failureReason.getDescription();
    }

    @NotNull CompletableFuture<Response<Account>> requestOrCreateAccount(
            @NotNull EconomyProvider provider, @NotNull String identifier
    ) {
        UUID uuid;
        try {
            uuid = UUID.fromString(identifier);
        } catch (IllegalArgumentException ignored) {
            return CompletableFuture.completedFuture(Response.failure(FailureReason.of(
                    "Invalid UUID of player account")));
        }
        return provider.accountAccessor().player().withUniqueId(uuid).genericGet();
    }

    boolean nonPlayer() {
        return false;
    }

}