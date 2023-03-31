/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.common.response.TreasuryException;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpongeToTreasuryCurrencyImpl implements Currency {

    private final String identifier;
    private final org.spongepowered.api.service.economy.Currency delegateSpongeCurrency;

    public SpongeToTreasuryCurrencyImpl(
            final String identifier,
            final org.spongepowered.api.service.economy.Currency delegateSpongeCurrency
    ) {
        this.identifier = identifier;
        this.delegateSpongeCurrency = delegateSpongeCurrency;
    }

    public org.spongepowered.api.service.economy.Currency getDelegateSpongeCurrency() {
        return this.delegateSpongeCurrency;
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    @Override
    public @NotNull String getSymbol() {
        return PlainTextComponentSerializer.plainText().serialize(delegateSpongeCurrency.symbol());
    }

    @Override
    public char getDecimal(@Nullable final Locale locale) {
        return '.';
    }

    @Override
    public @NotNull Map<Locale, Character> getLocaleDecimalMap() {
        return Collections.singletonMap(Locale.ENGLISH, '.');
    }

    @Override
    public @NotNull String getDisplayName(
            @NotNull final BigDecimal value, @Nullable final Locale locale
    ) {
        Component toSerialize;
        if (value.equals(BigDecimal.ONE)) {
            toSerialize = delegateSpongeCurrency.displayName();
        } else {
            toSerialize = delegateSpongeCurrency.pluralDisplayName();
        }
        return PlainTextComponentSerializer.plainText().serialize(toSerialize);
    }

    @Override
    public int getPrecision() {
        return delegateSpongeCurrency.defaultFractionDigits();
    }

    @Override
    public boolean isPrimary() {
        return delegateSpongeCurrency.isDefault();
    }

    @Override
    public @NotNull BigDecimal getStartingBalance(@NotNull final Account account) {
        // No starting balance for migrated currencies sponge -> treasury
        return BigDecimal.ZERO;
    }

    @Override
    public @NotNull BigDecimal getConversionRate() {
        // This is kinda sad imo, currencies coming from the Sponge API will not be able to get
        // properly converted in between (Sponge API currency -> Sponge API currency) when utilised
        // by the users of the treasury api.
        return BigDecimal.ONE;
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> parse(
            @NotNull final String formattedAmount, @Nullable final Locale locale
    ) {
        return FutureHelper.failedFuture(new TreasuryException(
                "Sponge Economy API migrated currency to Treasury Economy API. Currency of Sponge Economy API does not contain a parse method."));
    }

    @Override
    public @NotNull String format(@NotNull final BigDecimal amount, @Nullable final Locale locale) {
        return PlainTextComponentSerializer
                .plainText()
                .serialize(this.delegateSpongeCurrency.format(amount));
    }

    @Override
    public @NotNull String format(
            @NotNull final BigDecimal amount, @Nullable final Locale locale, final int precision
    ) {
        return PlainTextComponentSerializer
                .plainText()
                .serialize(this.delegateSpongeCurrency.format(amount, precision));
    }

}
