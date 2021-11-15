package me.lokka30.treasury.plugin.core.command.subcommand.migrate;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.response.FailureReason;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;
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
        this.currency = new Currency() {
            private final UUID uuid = UUID.randomUUID();

            @Override
            public @NotNull UUID getCurrencyId() {
                return uuid;
            }

            @Override
            public @NotNull String getCurrencyName() {
                return "MigrationMoney";
            }

            @Override
            public int getRoundedDigits() {
                return 0;
            }

            @Override
            public double getStartingBalance(@Nullable UUID playerUUID) {
                return 0;
            }

            @Override
            public @NotNull String formatBalance(double amount, @NotNull Locale locale) {
                return String.valueOf(amount);
            }
        };
        this.migrationException = new EconomyException(FailureReason.MIGRATION, "Economy unavailable during migration process.");
    }

    @Override
    public @NotNull EconomyAPIVersion getSupportedAPIVersion() {
        return TreasuryPlugin.getInstance().getEconomyAPIVersion();
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
