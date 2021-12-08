package me.lokka30.treasury.api.economy.misc;

/**
 * Represents a utility class for handling double values, or economy amounts.
 *
 * @since v1.0.0
 */
public final class AmountUtils {

    /**
     * If the specified amount is less than zero
     * then zero is returned. Otherwise, the amount
     * is returned. This ensures an amount is at least zero
     * since negative values are not allowed in the API.
     *
     * @param amount to check for.
     * @return the unmodified or modified amount.
     * @author lokka30
     * @since v1.0.0
     */
    public static double ensureAtLeastZero(final double amount) {
        return Math.max(amount, 0.0d);
    }

    private AmountUtils() {
        throw new IllegalArgumentException("Initialization of utility-type class");
    }
}
