/*
 * Copyright (c) 2021-2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
    void requestBalance(@NotNull Currency currency, @NotNull EconomySubscriber<Double> subscription);

    /**
     * Set the balance of the {@code Account}.
     *
     * <p>Specified amounts must be AT OR ABOVE zero.
     *
     * @author lokka30, Geolykt
     * @param amount the amount the new balance will be
     * @param currency the {@link Currency} of the balance being set
     * @param subscription the {@link EconomySubscriber} accepting the new balance
     * @see Account#requestBalance(Currency, EconomySubscriber)
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
     * @see Account#requestBalance(Currency, EconomySubscriber)
     * @since v1.0.0
     */
    default void canAfford(double amount, @NotNull Currency currency, @NotNull EconomySubscriber<Boolean> subscription) {
        requestBalance(currency, new EconomySubscriber<Double>() {
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
