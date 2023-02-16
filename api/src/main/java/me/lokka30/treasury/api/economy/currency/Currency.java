/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.account.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One of Treasury's core features is multi-currency support.
 * This allows economy providers and plugins to use different
 * currencies for different transactions, e.g. a daily reward
 * plugin can award players 'Tokens', but a job plugin
 * can award players 'Dollars'. This facilitates a high
 * potential for customization, to create a more interesting
 * economy.
 *
 * @author creatorfromhell, lokka30
 * @since v1.0.0
 */
public interface Currency {

    /**
     * Gets the unique non-user friendly identifier for the currency.
     *
     * @return A unique non-user friendly identifier for the currency.
     * @since v1.0.0
     */
    @NotNull String getIdentifier();

    /**
     * Gets the currency's symbol. This could be something as simple as a dollar sign '$'.
     *
     * @return The currency's symbol.
     * @since v1.0.0
     */
    @NotNull String getSymbol();

    /**
     * Gets the currency's decimal character for the specified {@link Locale locale} to be used for
     * formatting purposes.
     * <p>
     * The given {@code locale} may be {@code null} (unspecified), in this case, the decimal
     * character of this currency's default locale should be returned.
     * <p>
     * If the decimal character for a given {@code Locale} is not available, the decimal
     * character of this currency's default locale should be returned.
     *
     * @return decimal character of the given locale, if available (may fall back on a default
     *         locale).
     * @since v2.0.0
     */
    char getDecimal(@Nullable Locale locale);

    /**
     * Returns an immutable {@link Map} of each available {@link Currency#getDecimal(Locale)
     * decimal character}, mapped by the locale it belongs to.
     *
     * @return map of decimal characters against which locale they each belong to
     * @since v2.0.0
     */
    @NotNull Map<Locale, Character> getLocaleDecimalMap();

    /**
     * Gets this {@code Currency}'s user-friendly display name, based on the specified
     * {@link BigDecimal} {@code value}. If the value is <= 1, then it should return a singular form
     * of a display name. If the value is > 1, then it should return a plural form of it. Both
     * singular and plural forms can be mapped to a specific {@link Locale} via the
     * specified (if specified) {@code locale}. If a locale is not present, then it should
     * default to {@link Locale#ENGLISH}.
     *
     * @param value the value, on which depends whether a singular or a plural display name is
     *              returned
     * @param locale a locale
     * @return display name
     */
    @NotNull String getDisplayName(@NotNull BigDecimal value, @Nullable Locale locale);

    /**
     * Gets the currency's precision of fractional digits when formatting this currency.
     * <p>
     * This method concerns precision, not formatting. The economy provider is expected to handle
     * numbers internally with <i>x</i> fractional digits of precision.
     * <p>
     * For instance, if you are using the SQL data type {@code DECIMAL(38, 4)}, you are storing
     * numbers with a precision of {@code 4} fractional digits.
     * <p>
     * It is recommended that you have a precision of three or more fractional digits.
     *
     * @return The currency's default number of decimal digits when formatting this currency.
     * @since v1.0.0
     */
    int getPrecision();

    /**
     * Checks if this {@code Currency} is the primary currency to use of the
     * {@link me.lokka30.treasury.api.economy.EconomyProvider economy provider} this {@code
     * Currency} originated from.
     *
     * @return {@code true} if this currency is the primary currency of this economy provider,
     *         otherwise, {@code} false.
     * @since v1.0.0
     */
    boolean isPrimary();

    /**
     * Gets the starting balance of the specified {@link Account account} for this particular
     * {@code Currency}.
     * <p>
     * An economy provider may choose to have different starting balances of this
     * {@code Currency} on a per-{@link Account account} basis.
     *
     * @param account the account for which the starting balance shall be retrieved.
     * @return the starting balance of this currency for the specified account
     * @since v2.0.0
     */
    @NotNull BigDecimal getStartingBalance(@NotNull Account account);

    /**
     * Get the conversion rate of this {@code Currency}.
     * <p>
     * A conversion rate is a rate of how this currency will be converted to other currencies.
     * <p>
     * For example, the conversion rate of BGN and EUR may be {@code 0.511292} and {@code 0.884059},
     * respectively. By the default formula used in {@link #to(Currency, BigDecimal)}, the
     * conversion of 1 BGN -> EUR will result in {@code 0.51129188} EUR.
     *
     * @return conversion rate
     * @since v2.0.0
     */
    @NotNull BigDecimal getConversionRate();

    /**
     * Used to convert this {@link Currency} to another based on the {@link #getConversionRate()}
     * of this {@code Currency} and the specified one.
     *
     * @param currency The currency we are converting to.
     * @param amount   The amount to be converted to the specified {@link Currency}
     * @return converted balance
     * @since v1.0.0
     */
    @NotNull
    default BigDecimal to(@NotNull Currency currency, @NotNull BigDecimal amount) {
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(amount, "amount");

        return amount.multiply(this.getConversionRate()).divide(
                currency.getConversionRate(),
                RoundingMode.HALF_UP
        );
    }

    /**
     * Converts a formatted amount string (i.e., one generated via
     * {@link Currency#format(BigDecimal, Locale)}) into the BigDecimal value it represents,
     * according to the specified locale (nullable).
     * <p>
     * If a per-locale formatting system is not used by the currency, the parameter can be ignored.
     * If the specified locale is null, the currency implementation should assume a default.
     * <p>
     * If the specified string cannot be parsed, a {@link Response} with a suitable
     * {@link me.lokka30.treasury.api.common.response.FailureReason} is returned.
     *
     * @param formattedAmount formatted amount string to be parsed
     * @return future containing a {@link Response response} which, if successful, contains the
     *         resulting {@link BigDecimal} that is represented by the specified string .
     * @since v2.0.0
     */
    @NotNull CompletableFuture<Response<BigDecimal>> parse(
            @NotNull String formattedAmount, @Nullable Locale locale
    );

    /**
     * Translates the given {@link BigDecimal amount} to a human-readable representation as a
     * formatted string. The formatted string should be determined by the given
     * {@link Locale locale}.
     *
     * @param amount The amount to format.
     * @param locale The locale to use for formatting the balance. This value may be
     *               {@code null} if the provider should assume a default locale.
     * @return human-readable representation of the {@link BigDecimal amount} as a formatted string
     * @since v1.0.0
     */
    @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale);

    /**
     * Used to translate an amount to a user readable format with the specified amount of decimal places.
     * <p>
     * If a per-locale formatting system is not used by the currency, the parameter can be ignored -
     * likewise with the specified precision value.
     *
     * @param amount    The amount to format.
     * @param locale    The locale to use for formatting the balance. This value may be
     *                  {@code null} if the provider should provide their 'default' Locale.
     * @param precision The amount of decimal digits to use when formatting.
     * @return The formatted text.
     * @since v1.0.0
     */
    @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale, int precision);

}
