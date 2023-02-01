/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
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
import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.AccountData;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A dummy {@link EconomyProvider} used to prevent transactions with
 *
 * @author Jikoo, MrIvanPlays
 * @since v1.0.0
 */
class MigrationEconomy implements EconomyProvider {
    
    private static final FailureReason MIGRATION = () ->
            "The feature is currently not available during migration.";

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
            public char getDecimal(final @Nullable Locale locale) {
                return '.';
            }

            @Override
            public @NotNull String getDisplayNameSingular() {
                return getIdentifier();
            }

            @Override
            public @NotNull String getDisplayNamePlural() {
                return getIdentifier();
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
            public @NotNull BigDecimal getStartingBalance(@NotNull final Account account) {
                return BigDecimal.ZERO;
            }

            @Override
            public @NotNull BigDecimal getConversionRate() {
                return new BigDecimal(1);
            }

            @Override
            @NotNull
            public CompletableFuture<Response<BigDecimal>> parse(@NotNull final String formatted) {
                return CompletableFuture.completedFuture(Response.failure(MIGRATION));
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
                            @NotNull PlayerAccountCreateContext context
                    ) {
                        return CompletableFuture.completedFuture(Response.failure(
                                MIGRATION));
                    }
                };
            }

            @Override
            public @NotNull NonPlayerAccountAccessor nonPlayer() {
                return new NonPlayerAccountAccessor() {
                    @Override
                    protected @NotNull CompletableFuture<Response<NonPlayerAccount>> getOrCreate(
                            @NotNull NonPlayerAccountCreateContext context
                    ) {
                        return CompletableFuture.completedFuture(Response.failure(
                                MIGRATION));
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
    public @NotNull CompletableFuture<Response<TriState>> hasAccount(@NotNull AccountData accountData) {
        return CompletableFuture.completedFuture(Response.failure(MIGRATION));
    }

    @Override
    @NotNull
    public CompletableFuture<Response<Collection<UUID>>> retrievePlayerAccountIds() {
        return CompletableFuture.completedFuture(Response.failure(MIGRATION));
    }

    @Override
    @NotNull
    public CompletableFuture<Response<Collection<NamespacedKey>>> retrieveNonPlayerAccountIds() {
        return CompletableFuture.completedFuture(Response.failure(MIGRATION));
    }

    @Override
    public @NotNull Currency getPrimaryCurrency() {
        return currency;
    }

    @Override
    @NotNull
    public Optional<Currency> findCurrency(@NotNull final String identifier) {
        return Optional.empty();
    }

    @Override
    @NotNull
    public Set<Currency> getCurrencies() {
        return new HashSet<>();
    }

    @Override
    @NotNull
    public CompletableFuture<Response<TriState>> registerCurrency(@NotNull final Currency currency) {
        return CompletableFuture.completedFuture(Response.failure(MIGRATION));
    }

    @Override
    @NotNull
    public CompletableFuture<Response<TriState>> unregisterCurrency(@NotNull final Currency currency) {
        return CompletableFuture.completedFuture(Response.failure(MIGRATION));
    }

}
