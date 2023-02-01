/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
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
     * Gets the currency's decimal character to be used for formatting purposes.
     *
     * @return The currency's decimal character.
     * @since v1.0.0
     */
    char getDecimal();

    /**
     * Gets the singular form of the currency's user-friendly display name.
     * The singular form is used when the amount is exactly {@code 1.00}.
     *
     * @return The currency's user-friendly display name.
     * @since v1.0.0
     */
    @NotNull String getDisplayNameSingular();

    /**
     * Gets the plural form of the currency's user-friendly display name.
     * The plural form is used when the amount is not exactly {@code 1.00}.
     * If a plural form of the {@link Currency} is not available, the value from
     * {@link Currency#getDisplayNameSingular()} should be returned.
     *
     * @return The plural form of the currency's user-friendly display name.
     * @since v1.0.0
     */
    @NotNull String getDisplayNamePlural();

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
     * Checks if this currency is the primary currency to use of this economy provider.
     *
     * @return {@code true} if this currency is the primary currency of this economy provider,
     *         otherwise, {@code} false.
     * @since v1.0.0
     */
    boolean isPrimary();

    /**
     * Gets the starting balance of the specified {@link Account account} for this particular
     * {@link Currency currency}.
     * <p>
     * An economy provider may choose to have different starting balances of this
     * {@link Currency currency} on a per-{@link Account account} basis.
     *
     * @param account the account for which the starting balance shall be retrieved.
     * @return the starting balance of this currency for the specified account
     */
    @NotNull
    BigDecimal getStartingBalance(@NotNull Account account);

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
     * @since 2.0.0
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

        return amount
                .multiply(this.getConversionRate())
                .divide(currency.getConversionRate(), RoundingMode.HALF_UP);
    }

    /**
     * Used to get the BigDecimal representation of an amount represented by a formatted string.
     * If the specified string cannot be parsed, a {@link Response} with a suitable
     * {@link me.lokka30.treasury.api.common.response.FailureReason} shall be returned.
     *
     * @param formatted The formatted string to be converted to BigDecimal form.
     * @return future with {@link Response} which if successful returns the resulting
     *         {@link BigDecimal} that represents the deformatted amount of the formatted String.
     * @since v1.0.0
     */
    @NotNull
    CompletableFuture<Response<BigDecimal>> parse(@NotNull String formatted);

    /**
     * Used to translate an amount to a user readable format with the default precision.
     * If a per-locale format is not used, simply ignore the parameter.
     *
     * @param amount The amount to format.
     * @param locale The locale to use for formatting the balance. This value may be
     *               {@code null} if the provider should provide their 'default' Locale.
     * @return The formatted text.
     * @since v1.0.0
     */
    @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale);

    /**
     * Used to translate an amount to a user readable format with the specified amount of decimal places.
     * If a per-locale format is not used, simply ignore the parameter.
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
