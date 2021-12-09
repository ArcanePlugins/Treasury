/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.currency;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * One of Treasury's core features is multi-currency support.
 * This allows economy providers and plugins to use different
 * currencies for different transactions, e.g. a daily reward
 * plugin can award players 'Tokens', but a job plugin
 * can award players 'Dollars'. Facilitates great customisability.
 *
 * @author lokka30, Geolykt, MrIvanPlays
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public class Currency {

    /**
     * Creates a new currency with random {@link UUID} {@code currencyId} and a {@code startingBalance} handler of
     * always returning 0.
     *
     * @param currencyChar          a way to identify the currency via a character e.g. '$' for a dollar. can be null.
     * @param roundedDigits         to which digit shall we round balances. can specify -1 for no rounding.
     * @param conversionCoefficient coefficient at which this currency is going to be converted to other currencies.
     * @param balanceFormatter      a function, giving a human-readable format of a balance.
     * @param names                 acceptable names of the currency. primary name shall be the first specified.
     * @return new currency instance
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    public static Currency of(
            @Nullable Character currencyChar,
            int roundedDigits,
            double conversionCoefficient,
            @NotNull BiFunction<Double, Locale, String> balanceFormatter,
            @NotNull String @NotNull... names
    ) {
        return new Currency(
                null,
                currencyChar,
                roundedDigits,
                conversionCoefficient,
                null,
                balanceFormatter,
                names
        );
    }

    /**
     * Creates a new currency.
     *
     * @param currencyId            the currency id.
     * @param currencyChar          a way to identify the currency via a character e.g. '$' for a dollar. can be null.
     * @param roundedDigits         to which digit shall we round balances. can specify -1 for no rounding.
     * @param conversionCoefficient coefficient at which this currency is going to be converted to other currencies.
     * @param startingBalance       a function, giving a starting balance for the inputted player uuid, which can be null.
     * @param balanceFormatter      a function, giving a human-readable format of a balance.
     * @param names                 acceptable names of the currency. primary name shall be the first specified.
     * @return new currency instance
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    public static Currency of(
            @NotNull UUID currencyId,
            @Nullable Character currencyChar,
            int roundedDigits,
            double conversionCoefficient,
            @Nullable Function<UUID, Double> startingBalance,
            @NotNull BiFunction<Double, Locale, String> balanceFormatter,
            @NotNull String @NotNull... names
    ) {
        Objects.requireNonNull(currencyId, "currencyId");
        return new Currency(
                currencyId,
                currencyChar,
                roundedDigits,
                conversionCoefficient,
                startingBalance,
                balanceFormatter,
                names
        );
    }

    private final UUID currencyId;
    private final Character currencyChar;
    private final String[] names;
    private final int roundedDigits;
    private double conversionCoefficient;
    private final Function<UUID, Double> startingBalance;
    private final BiFunction<Double, Locale, String> balanceFormatter;

    private Currency(@Nullable UUID currencyId,
                     @Nullable Character currencyChar,
                     int roundedDigits,
                     double conversionCoefficient,
                     @Nullable Function<UUID, Double> startingBalance,
                     @NotNull BiFunction<Double, Locale, String> balanceFormatter,
                     @NotNull String @NotNull... names
    ) {
        if (names.length < 1) {
            throw new IllegalArgumentException("Empty array specified for currency name.");
        }
        this.balanceFormatter = Objects.requireNonNull(balanceFormatter, "balanceFormatter");
        this.names = Objects.requireNonNull(names, "names");
        this.currencyId = currencyId == null ? UUID.randomUUID() : currencyId;
        this.currencyChar = currencyChar;
        this.roundedDigits = roundedDigits;
        this.conversionCoefficient = conversionCoefficient;
        this.startingBalance = startingBalance == null ? ($) -> 0d : startingBalance;
    }

    /**
     * Gets the {@link UUID} of the currency.
     *
     * @return the UUID of the currency
     * @author lokka30, MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    public UUID getCurrencyId() {
        return currencyId;
    }

    /**
     * Gets the character which identifies this currency, which may be null.
     *
     * @return char identifier. could be null
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @Nullable
    public Character getCurrencyCharacter() {
        return currencyChar;
    }

    /**
     * Get the primary name of the currency, e.g. 'dollar' or 'token'.
     *
     * @return the name of the currency
     * @author lokka30, MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    public String getPrimaryCurrencyName() {
        return names[0];
    }

    /**
     * Get the acceptable currency names. Used for parsing.
     *
     * @return acceptable currency names
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    public String @NotNull [] getCurrencyNames() {
        return names;
    }

    /**
     * Some economy providers like to round balances' decimals.
     * Economy providers that do not round any digits should specify `-1`.
     *
     * @return how many rounded digits the provider uses, or `-1` for none
     * @author lokka30, MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public int getRoundedDigits() {
        return roundedDigits;
    }

    /**
     * Get the conversion coefficient of this currency.
     *
     * @return conversion coefficient
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public double getConversionCoefficient() {
        return conversionCoefficient;
    }

    /**
     * Sets a new conversion coefficient of this currency.
     *
     * @param conversionCoefficient new conversion coefficient
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    protected void setConversionCoefficient(double conversionCoefficient) {
        this.conversionCoefficient = conversionCoefficient;
    }

    /**
     * Get the starting balance of the currency.
     * The player {@link UUID} is nullable, it should be specified if the starting balance
     * concerns a {@link me.lokka30.treasury.api.economy.account.PlayerAccount}.
     *
     * @param playerUUID a UUID of the player account created. For global scenarios, specify `null`.
     * @return the starting balance of the currency concerning specified player's UUID.
     * @author lokka30, Geolykt, MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public double getStartingBalance(@Nullable UUID playerUUID) {
        return startingBalance.apply(playerUUID);
    }

    /**
     * Gets a human-readable format of the balance.
     * For example, '$1.50' or '1.50 dollars' or 'One dollar and fifty cents'.
     *
     * @param amount to be formatted.
     * @param locale of the formatted balance being requested.
     * @return the human-readable format of the specified amount and locale.
     * @author lokka30, MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    public String formatBalance(double amount, @NotNull Locale locale) {
        Objects.requireNonNull(locale, "locale");
        return balanceFormatter.apply(amount, locale);
    }
}
