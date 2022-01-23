/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomyFailureReason;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            @Override
            public String getIdentifier() {
                return "MigrationMoney";
            }

            @Override
            public String getSymbol() {
                return "$";
            }

            @Override
            public char getDecimal() {
                return 0;
            }

            @Override
            public String getDisplayNameSingular() {
                return "MigrationMoney";
            }

            @Override
            public String getDisplayNamePlural() {
                return "MigrationMonies";
            }

            @Override
            public int getPrecision() {
                return 0;
            }

            @Override
            public boolean isPrimary() {
                return false;
            }

            @Override
            public void to(
                    @NotNull final Currency currency,
                    final BigDecimal amount,
                    @NotNull final EconomySubscriber<BigDecimal> subscription
            ) {
                subscription.fail(new EconomyException(EconomyFailureReason.MIGRATION,
                        "Migration currency not convertable."
                ));
            }

            @Override
            public void parse(
                    @NotNull final String formatted,
                    @NotNull final EconomySubscriber<BigDecimal> subscription
            ) {
                subscription.fail(new EconomyException(EconomyFailureReason.MIGRATION,
                        "Migration in progress, cannot deformat!"
                ));
            }

            @Override
            public BigDecimal getStartingBalance(@Nullable final UUID playerID) {
                return BigDecimal.ZERO;
            }

            @Override
            public String format(final BigDecimal amount, @Nullable final Locale locale) {
                return amount.toPlainString();
            }

            @Override
            public String format(
                    final BigDecimal amount, @Nullable final Locale locale, final int precision
            ) {
                return amount.toPlainString();
            }
        };
        this.migrationException = new EconomyException(EconomyFailureReason.MIGRATION,
                "Economy unavailable during migration process."
        );

    }

    @Override
    public @NotNull EconomyAPIVersion getSupportedAPIVersion() {
        //noinspection deprecation
        return EconomyAPIVersion.getCurrentAPIVersion();
    }

    @Override
    @NotNull
    public Set<OptionalEconomyApiFeature> getSupportedOptionalEconomyApiFeatures() {
        return new HashSet<>(Collections.singletonList(OptionalEconomyApiFeature.NEGATIVE_BALANCES));
    }

    @Override
    public void hasPlayerAccount(
            @NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription
    ) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrievePlayerAccount(
            @NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription
    ) {
        subscription.fail(migrationException);
    }

    @Override
    public void createPlayerAccount(
            @NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription
    ) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrievePlayerAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void hasAccount(
            @NotNull String accountId, @NotNull EconomySubscriber<Boolean> subscription
    ) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveAccount(
            @NotNull String accountId, @NotNull EconomySubscriber<Account> subscription
    ) {
        subscription.fail(migrationException);
    }

    @Override
    public void createAccount(
            @Nullable String name,
            @NotNull String accountId,
            @NotNull EconomySubscriber<Account> subscription
    ) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveAccountIds(@NotNull EconomySubscriber<Collection<String>> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveNonPlayerAccountIds(@NotNull EconomySubscriber<Collection<String>> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public @NotNull Currency getPrimaryCurrency() {
        return currency;
    }

    @Override
    public Optional<Currency> findCurrency(@NotNull final String identifier) {
        return Optional.empty();
    }

    @Override
    public Set<Currency> getCurrencies() {
        return new HashSet<>();
    }

    @Override
    public void registerCurrency(
            @NotNull final Currency currency, @NotNull final EconomySubscriber<Boolean> subscription
    ) {
        subscription.fail(new EconomyException(EconomyFailureReason.MIGRATION,
                "Cannot register currencies during migration!"
        ));
    }

}
