package me.lokka30.treasury.api.economy.misc;

// TODO: What shall we call this?
public class APIUtils {

    /**
     * @author lokka30
     * @since v1.0.0
     * If the specified amount is less than zero
     * then zero is returned. Otherwise, the amount
     * is returned. This ensures an amount is at least zero
     * since negative values are not allowed in the API.
     * @param amount to check for.
     * @return the unmodified or modified amount.
     */
    public static double ensureAtLeastZero(final double amount) {
        return Math.max(amount, 0.0d);
    }
}
