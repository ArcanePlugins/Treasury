/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.response.FailureReason;
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
            public String identifier() {
                return "MigrationMoney";
            }

            @Override
            public String symbol() {
                return "$";
            }

            @Override
            public char decimal() {
                return 0;
            }

            @Override
            public String display() {
                return "MigrationMoney";
            }

            @Override
            public String displayPlural() {
                return "MigrationMonies";
            }

            @Override
            public int precision() {
                return 0;
            }

            @Override
            public boolean isDefault() {
                return false;
            }

            @Override
            public boolean isDefault(@NotNull final String world) {
                return false;
            }

            @Override
            public void to(
                    @NotNull final Currency currency,
                    @NotNull final Double amount,
                    @NotNull final EconomySubscriber<Double> subscription
            ) {
                subscription.fail(new EconomyException(FailureReason.MIGRATION, "Migration currency not convertable."));
            }

            @Override
            public void deformat(@NotNull final String formatted, @NotNull final EconomySubscriber<Double> subscription) {
                subscription.fail(new EconomyException(FailureReason.MIGRATION, "Migration in progress, cannot deformat!"));
            }

            @Override
            public double getStartingBalance(@Nullable final UUID playerID) {
                return 0;
            }

            @Override
            public String format(@NotNull final Double amount) {
                return String.valueOf(amount);
            }

            @Override
            public String format(@NotNull final Double amount, @NotNull final Integer precision) {
                return String.valueOf(amount);
            }
        };
        this.migrationException = new EconomyException(FailureReason.MIGRATION, "Economy unavailable during migration process.");
    }

    @Override
    public @NotNull EconomyAPIVersion getSupportedAPIVersion() {
        return EconomyAPIVersion.getCurrentAPIVersion();
    }

    @Override
    @NotNull
    public Set<OptionalEconomyApiFeature> getSupportedOptionalEconomyApiFeatures() {
        return new HashSet<>(Collections.singletonList(OptionalEconomyApiFeature.NEGATIVE_BALANCES));
    }

    @Override
    public void hasPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription) {
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
    public void hasBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public void createBankAccount(
            @Nullable String name, @NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription
    ) {
        subscription.fail(migrationException);
    }

    @Override
    public void retrieveBankAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription) {
        subscription.fail(migrationException);
    }

    @Override
    public @NotNull Currency getPrimaryCurrency() {
        return currency;
    }

    /**
     * Used to find a currency based on a specific identifier.
     *
     * @param identifier The {@link Currency#identifier()} of the {@link Currency} we are searching for.
     *
     * @return The {@link Optional} containing the search result. This will contain the
     * resulting {@link Currency} if it exists, otherwise it will return {@link Optional#empty()}.
     * @author creatorfromhell
     * @since {@link EconomyAPIVersion#v1_0 v1.0}
     */
    @Override
    public Optional<Currency> findCurrency(@NotNull final String identifier) {
        return Optional.empty();
    }

    /**
     * Used to get a set of every  {@link Currency} object for the server.
     *
     * @return A set of every {@link Currency} object that is available for the server.
     * @author creatorfromhell
     * @since {@link EconomyAPIVersion#v1_0 v1.0}
     */
    @Override
    public Set<Currency> getCurrencies() {
        return new HashSet<>();
    }

    /**
     * Used to register a currency with the {@link EconomyProvider} to be utilized by
     * other plugins.
     *
     * @param currency     The currency to register with the {@link EconomyProvider}.
     * @param subscription The {@link EconomySubscriber} representing the result of the
     *                     attempted {@link Currency} registration with an {@link Boolean}.
     *                     This will be {@link Boolean#TRUE} if it was registered, otherwise
     *                     it'll be {@link Boolean#FALSE}.
     *
     * @author creatorfromhell
     * @since {@link EconomyAPIVersion#v1_0 v1.0}
     */
    @Override
    public void registerCurrency(@NotNull final Currency currency, @NotNull final EconomySubscriber<Boolean> subscription) {
subscription.fail(new EconomyException(FailureReason.MIGRATION, "Cannot register currencies during migration!"));
    }

}
