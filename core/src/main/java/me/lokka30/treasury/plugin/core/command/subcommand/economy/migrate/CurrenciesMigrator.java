/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CurrenciesMigrator implements Runnable {

    private final MigrationData migration;

    public CurrenciesMigrator(@NotNull MigrationData migration) {
        this.migration = migration;
    }

    @Override
    public void run() {
        this.migration.debug(() -> "Starting migration of currencies");
        Set<Currency> currencies = this.migration.from().getCurrencies();
        EconomyProvider to = this.migration.to();
        Set<String> toCurrencies = to.getCurrencies().stream().map(Currency::getIdentifier).collect(
                Collectors.toSet());

        Map<String, ExecutionException> errors = new HashMap<>();

        for (Currency currency : currencies) {
            if (toCurrencies.contains(currency.getIdentifier())) {
                continue;
            }

            if (currency.isPrimary()) {
                currency = this.makeNonPrimary(currency);
            }

            try {
                String id = currency.getIdentifier();
                Response<TriState> response = to.registerCurrency(currency).get();
                if (!response.isSuccessful()) {
                    this.migration.debug(() -> "Currency '" + id + "' was not migrated because '" + response
                            .getFailureReason()
                            .getDescription() + "'");
                    continue;
                }

                if (response.getResult() != TriState.TRUE) {
                    this.migration.debug(() -> "Currency '" + id + "' was not migrated for unknown reason.");
                    continue;
                }

                this.migration.migratedCurrencies().add(id);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                errors.put(currency.getIdentifier(), e);
            }
        }

        if (!errors.isEmpty()) {
            String currenciesStr = String.join(", ", errors.keySet());
            RuntimeException e = new RuntimeException("Errors occurred whilst migrating currencies '" + currenciesStr + "'");
            for (Map.Entry<String, ExecutionException> entry : errors.entrySet()) {
                e.addSuppressed(new RuntimeException("currency: " + entry.getKey(),
                        entry.getValue()
                ));
            }
            throw e;
        }
        this.migration.debug(() -> "Finished migrating currencies without any errors");
    }

    private Currency makeNonPrimary(Currency c) {
        return new Currency() {
            @Override
            public @NotNull String getIdentifier() {
                return c.getIdentifier();
            }

            @Override
            public @NotNull String getSymbol() {
                return c.getSymbol();
            }

            @Override
            public char getDecimal() {
                return c.getDecimal();
            }

            @Override
            public @NotNull String getDisplayNameSingular() {
                return c.getDisplayNameSingular();
            }

            @Override
            public @NotNull String getDisplayNamePlural() {
                return c.getDisplayNamePlural();
            }

            @Override
            public int getPrecision() {
                return c.getPrecision();
            }

            @Override
            public boolean isPrimary() {
                return false;
            }

            @Override
            public boolean supportsNegativeBalances() {
                return c.supportsNegativeBalances();
            }

            @Override
            public CompletableFuture<Response<BigDecimal>> to(
                    @NotNull Currency currency,
                    @NotNull BigDecimal amount
            ) {
                return c.to(currency, amount);
            }

            @Override
            public CompletableFuture<Response<BigDecimal>> parse(@NotNull String formatted) {
                return c.parse(formatted);
            }

            @Override
            public @NotNull BigDecimal getStartingBalance(@Nullable UUID playerID) {
                return c.getStartingBalance(playerID);
            }

            @Override
            public @NotNull String format(
                    @NotNull BigDecimal amount,
                    @Nullable Locale locale
            ) {
                return c.format(amount, locale);
            }

            @Override
            public @NotNull String format(
                    @NotNull BigDecimal amount,
                    @Nullable Locale locale,
                    final int precision
            ) {
                return c.format(amount, locale, precision);
            }
        };
    }

}
