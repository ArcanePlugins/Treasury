/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.AccountData;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyFailureReason;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A dummy {@link EconomyProvider} used to prevent transactions with
 *
 * @author Jikoo, MrIvanPlays
 * @since v1.0.0
 */
class MigrationEconomy implements EconomyProvider {

    private final @NotNull Currency currency;
    private final @NotNull AccountAccessor accountAccessor;

    MigrationEconomy() {
        this.currency = new Currency() {
            @Override
            public @NotNull String getIdentifier() {
                return "MigrationMoney";
            }

            @Override
            public @NotNull String getSymbol() {
                return "$";
            }

            @Override
            public char getDecimal() {
                return 0;
            }

            @Override
            public @NotNull String getDisplayNameSingular() {
                return "MigrationMoney";
            }

            @Override
            public @NotNull String getDisplayNamePlural() {
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
            public TriState supportsNegativeBalances() {
                return TriState.UNSPECIFIED;
            }

            @Override
            public CompletableFuture<Response<BigDecimal>> to(
                    @NotNull final Currency currency, @NotNull final BigDecimal amount
            ) {
                return CompletableFuture.completedFuture(Response.failure(EconomyFailureReason.MIGRATION));
            }

            @Override
            public CompletableFuture<Response<BigDecimal>> parse(@NotNull final String formatted) {
                return CompletableFuture.completedFuture(Response.failure(EconomyFailureReason.MIGRATION));
            }

            @Override
            @NotNull
            public BigDecimal getStartingBalance(@Nullable final UUID playerID) {
                return BigDecimal.ZERO;
            }

            @Override
            @NotNull
            public String format(
                    final @NotNull BigDecimal amount, final @Nullable Locale locale
            ) {
                return amount.toPlainString();
            }

            @Override
            @NotNull
            public String format(
                    final @NotNull BigDecimal amount,
                    final @Nullable Locale locale,
                    final int precision
            ) {
                return amount.toPlainString();
            }
        };
        this.accountAccessor = new AccountAccessor() {
            @Override
            public @NotNull PlayerAccountAccessor player() {
                return new PlayerAccountAccessor() {
                    @Override
                    protected @NotNull CompletableFuture<Response<PlayerAccount>> getOrCreate(
                            @NotNull final UUID uniqueId
                    ) {
                        return CompletableFuture.completedFuture(Response.failure(
                                EconomyFailureReason.MIGRATION));
                    }
                };
            }

            @Override
            public @NotNull NonPlayerAccountAccessor nonPlayer() {
                return new NonPlayerAccountAccessor() {
                    @Override
                    protected @NotNull CompletableFuture<Response<NonPlayerAccount>> getOrCreate(
                            @NotNull final String identifier, @Nullable final String name
                    ) {
                        return CompletableFuture.completedFuture(Response.failure(
                                EconomyFailureReason.MIGRATION));
                    }
                };
            }
        };
    }

    @Override
    public @NotNull AccountAccessor accountAccessor() {
        return this.accountAccessor;
    }

    @Override
    public @NotNull CompletableFuture<Response<TriState>> hasAccount(final AccountData accountData) {
        return CompletableFuture.completedFuture(Response.failure(EconomyFailureReason.MIGRATION));
    }

    @Override
    public CompletableFuture<Response<Collection<UUID>>> retrievePlayerAccountIds() {
        return CompletableFuture.completedFuture(Response.failure(EconomyFailureReason.MIGRATION));
    }

    @Override
    public CompletableFuture<Response<Collection<String>>> retrieveAccountIds() {
        return CompletableFuture.completedFuture(Response.failure(EconomyFailureReason.MIGRATION));
    }

    @Override
    public CompletableFuture<Response<Collection<String>>> retrieveNonPlayerAccountIds() {
        return CompletableFuture.completedFuture(Response.failure(EconomyFailureReason.MIGRATION));
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
    public CompletableFuture<Response<TriState>> registerCurrency(@NotNull final Currency currency) {
        return CompletableFuture.completedFuture(Response.failure(EconomyFailureReason.MIGRATION));
    }

}
