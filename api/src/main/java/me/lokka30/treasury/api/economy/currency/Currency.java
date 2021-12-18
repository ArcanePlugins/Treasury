package me.lokka30.treasury.api.economy.currency;

import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public interface Currency {

    /**
     * Gets the unique non-user friendly identifier for the currency.
     *
     * @return A unique non-user friendly identifier for the currency.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String identifier();

    /**
     * Gets the currency's symbol. This could be something as simple as a dollar sign '$'.
     *
     * @return The currency's symbol.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String symbol();

    /**
     * Get's the currency's decimal character to be used for formatting purposes.
     *
     * @return The currency's decimal character.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    char decimal();

    /**
     * Gets the currency's user-friendly display name.
     *
     * @return The currency's user-friendly display name.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String display();

    /**
     * Gets the plural form of the currency's user-friendly display name.
     *
     * @return The plural form of the currency's user-friendly display name.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String displayPlural();

    /**
     * Gets the currency's default number of decimal digits when formatting this currency.
     *
     * @return The currency's default number of decimal digits when formatting this currency.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    int precision();

    /**
     * Checks if this currency is the default currency to use in a global context.
     *
     * @return True if this currency is the default currency. This method should use a global
     * context if multi-world support is not present, otherwise it should use the default world
     * for this check.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    boolean isDefault();

    /**
     * Checks if this currency is the default currency in a specific world.
     *
     * @param world The name of the world to use for the check.
     * @return True if this currency is the default currency for the specified world. This
     * method should default to a global context if multi-world support is not present. If the world
     * does not exist, this should default to the default world.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    boolean isDefault(@NotNull String world);

    /**
     * Used to convert this {@link Currency} to another based on a specified amount of the other
     * currency.
     *
     * @param currency The currency we are converting to.
     * @param amount The amount to be converted to the specified {@link Currency}
     * @param subscription The {@link EconomySubscriber} accepting the resulting {@link Double} that
     *                     represents the converted amount of the specified {@link Currency}.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void to(@NotNull Currency currency, @NotNull Double amount, @NotNull EconomySubscriber<Double> subscription);

    /**
     * Used to get the double representation of an amount represented by a formatted string.
     *
     * @param formatted The formatted string to be converted to double form.
     * @param subscription The {@link EconomySubscriber} accepting the resulting {@link Double} that
     *                     represents the deformatted amount of the formatted String.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void deformat(@NotNull String formatted, @NotNull EconomySubscriber<Double> subscription);

    /**
     * Gets the starting balance of a specific player account for this currency.
     *
     * @param playerID The UUID of the player we are getting the starting balance for.
     * @return The starting balance of the player for this currency.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    double getStartingBalance(@Nullable UUID playerID);

    /**
     * Used to translate an amount to a user readable format with the default precision.
     *
     * @param amount The amount to format.
     * @return The formatted text.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String format(@NotNull Double amount);

    /**
     * Used to translate an amount to a user readable format with the specified amount of decimal places.
     *
     * @param amount The amount to format.
     * @param precision The amount of decimal digits to use when formatting.
     * @return The formatted text.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    String format(@NotNull Double amount, @NotNull Integer precision);
}
