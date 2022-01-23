/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.currency;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One of Treasury's core features is multi-currency support.
 * This allows economy providers and plugins to use different
 * currencies for different transactions, e.g. a daily reward
 * plugin can award players 'Tokens', but a job plugin
 * can award players 'Dollars'. Facilitates great customisability.
 *
 * @author creatorfromhell
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface Currency {

    /**
     * Gets the unique non-user friendly identifier for the currency.
     *
     * @return A unique non-user friendly identifier for the currency.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String getIdentifier();

    /**
     * Gets the currency's symbol. This could be something as simple as a dollar sign '$'.
     *
     * @return The currency's symbol.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String getSymbol();

    /**
     * Get's the currency's decimal character to be used for formatting purposes.
     *
     * @return The currency's decimal character.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    char getDecimal();

    /**
     * Gets the currency's user-friendly display name.
     *
     * @return The currency's user-friendly display name.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String getDisplayNameSingular();

    /**
     * Gets the plural form of the currency's user-friendly display name.
     *
     * @return The plural form of the currency's user-friendly display name.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String getDisplayNamePlural();

    /**
     * Gets the currency's default number of decimal digits when formatting this currency.
     *
     * @return The currency's default number of decimal digits when formatting this currency.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    int getPrecision();

    /**
     * Checks if this currency is the primary currency to use.
     *
     * @return True if this currency is the primary currency. This method should use a global
     *         context if multi-world support is not present, otherwise it should use the default world
     *         for this check.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    boolean isPrimary();

    /**
     * Used to convert this {@link Currency} to another based on a specified amount of the other
     * currency.
     *
     * @param currency     The currency we are converting to.
     * @param amount       The amount to be converted to the specified {@link Currency}
     * @param subscription The {@link EconomySubscriber} accepting the resulting {@link BigDecimal} that
     *                     represents the converted amount of the specified {@link Currency}.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void to(
            @NotNull Currency currency,
            BigDecimal amount,
            @NotNull EconomySubscriber<BigDecimal> subscription
    );

    /**
     * Used to get the BigDecimal representation of an amount represented by a formatted string.
     *
     * @param formatted    The formatted string to be converted to BigDecimal form.
     * @param subscription The {@link EconomySubscriber} accepting the resulting {@link BigDecimal} that
     *                     represents the deformatted amount of the formatted String.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void parse(@NotNull String formatted, @NotNull EconomySubscriber<BigDecimal> subscription);

    /**
     * Gets the starting balance of a specific player account for this currency.
     *
     * @param playerID The UUID of the player we are getting the starting balance for.
     * @return The starting balance of the player for this currency.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    BigDecimal getStartingBalance(@Nullable UUID playerID);

    /**
     * Used to translate an amount to a user readable format with the default precision.
     *
     * @param amount The amount to format.
     * @param locale The locale to use for formatting the balance. This value may be null if the
     *               provider should provide the default Locale.
     * @return The formatted text.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String format(BigDecimal amount, @Nullable Locale locale);

    /**
     * Used to translate an amount to a user readable format with the specified amount of decimal places.
     *
     * @param amount    The amount to format.
     * @param locale    The locale to use for formatting the balance. This value may be null if the
     *                  *               provider should provide the default Locale.
     * @param precision The amount of decimal digits to use when formatting.
     * @return The formatted text.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String format(BigDecimal amount, @Nullable Locale locale, int precision);

}
