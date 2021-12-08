/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * An Account is something that holds a balance and is associated with
 * something bound by a UUID. For example, a PlayerAccount is bound to
 * a Player on a server by their UUID.
 *
 * @author lokka30, Geolykt
 * @see EconomyProvider
 * @see PlayerAccount
 * @see BankAccount
 * @since v1.0.0
 */
@SuppressWarnings({"unused"})
public interface Account {

    /**
     * Get the {@link UUID} of the {@code Account}.
     *
     * @author lokka30
     * @return uuid of the Account.
     * @see UUID
     * @since v1.0.0
     */
    @NotNull UUID getUniqueId();

    /**
     * Request the balance of the {@code Account}.
     *
     * @author lokka30, Geolykt
     * @param currency the {@link Currency} of the balance being requested
     * @param subscription the {@link EconomySubscriber} accepting the amount
     * @see Account#setBalance(double, Currency, EconomySubscriber)
     * @since v1.0.0
     */
    void retrieveBalance(@NotNull Currency currency, @NotNull EconomySubscriber<Double> subscription);

    /**
     * Set the balance of the {@code Account}.
     *
     * <p>Specified amounts must be AT OR ABOVE zero.
     *
     * @author lokka30, Geolykt
     * @param amount the amount the new balance will be
     * @param currency the {@link Currency} of the balance being set
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @see Account#retrieveBalance(Currency, EconomySubscriber)
     * @since v1.0.0
     */
    void setBalance(double amount, @NotNull Currency currency, @NotNull EconomySubscriber<Double> subscription);

    /**
     * Withdraw an amount from the {@code Account} balance.
     *
     * <p>Specified amounts must be ABOVE zero.
     *
     * @author lokka30, Geolykt
     * @param amount the amount the balance will be reduced by
     * @param currency the {@link Currency} of the balance being modified
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @see Account#setBalance(double, Currency, EconomySubscriber)
     * @since v1.0.0
     */
    void withdrawBalance(double amount, @NotNull Currency currency, @NotNull EconomySubscriber<Double> subscription);

    /**
     * Deposit an amount into the {@code Account} balance.
     *
     * <p>Specified amounts must be ABOVE zero.
     *
     * @author lokka30
     * @param amount the amount the balance will be increased by
     * @param currency the {@link Currency} of the balance being modified
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @see Account#setBalance(double, Currency, EconomySubscriber)
     * @since v1.0.0
     */
    void depositBalance(double amount, @NotNull Currency currency, @NotNull EconomySubscriber<Double> subscription);

    /**
     * Reset the {@code Account} balance to its starting amount.
     *
     * <p>Certain implementations, such as the {@link PlayerAccount}, may default to non-zero starting balances.
     *
     * @author lokka30, Geolykt
     * @param currency the {@link Currency} of the balance being reset
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @see PlayerAccount#resetBalance(Currency, EconomySubscriber)
     * @see Account#setBalance(double, Currency, EconomySubscriber)
     * @since v1.0.0
     */
    default void resetBalance(@NotNull Currency currency, @NotNull EconomySubscriber<Double> subscription) {
        setBalance(0.0d, currency, new EconomySubscriber<Double>() {
                @Override
                public void succeed(@NotNull Double value) {
                    subscription.succeed(0.0d);
                }

                @Override
                public void fail(@NotNull EconomyException exception) {
                    subscription.fail(exception);
                }
            }
        );
    }

    /**
     * Check if the {@code Account} can afford a withdrawal of a certain amount.
     *
     * <p>Specified amounts must be ABOVE zero.
     *
     * @author lokka30, Geolykt
     * @param amount the amount the balance must meet or exceed
     * @param currency the {@link Currency} of the balance being queried
     * @param subscription the {@link EconomySubscriber} accepting whether the balance is high enough
     * @see Account#retrieveBalance(Currency, EconomySubscriber)
     * @since v1.0.0
     */
    default void canAfford(double amount, @NotNull Currency currency, @NotNull EconomySubscriber<Boolean> subscription) {
        retrieveBalance(currency, new EconomySubscriber<Double>() {
                @Override
                public void succeed(@NotNull Double value) {
                    subscription.succeed(value >= amount);
                }

                @Override
                public void fail(@NotNull EconomyException exception) {
                    subscription.fail(exception);
                }
            }
        );
    }

    /**
     * Delete data stored for the {@code Account}.
     *
     * <p>Providers should consider storing backups of deleted accounts.
     *
     * @author lokka30
     * @param subscription the {@link EconomySubscriber} accepting whether deletion occurred successfully
     * @since v1.0.0
     */
    void deleteAccount(@NotNull EconomySubscriber<Boolean> subscription);

}
