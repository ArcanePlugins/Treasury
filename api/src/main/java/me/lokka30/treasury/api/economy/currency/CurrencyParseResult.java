/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.api.economy.currency;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Represents a result of parsing an {@link String} input to a
 * {@link Double} {@code value} and a {@link Currency} {@code currency}.
 *
 * <p>Example: "$20", "USD20", "dola20" ; this gets parsed to currency of 'dollar' and value of '20' if there is such
 * currency registered with a character '$' and the names specified.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public final class CurrencyParseResult {

    /**
     * Represents a stated result of a {@link CurrencyParseResult}
     *
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public enum State {

        /**
         * State indicates successful parse.
         *
         * @since v1.0.0
         */
        SUCCESS,

        /**
         * State indicates unknown currency was inputted.
         *
         * @since v1.0.0
         */
        UNKNOWN_CURRENCY,

        /**
         * State indicates invalid value was inputted. An invalid value may be a value which does not
         * consist of numbers.
         *
         * @since v1.0.0
         */
        INVALID_VALUE
    }

    /**
     * Creates a new {@link CurrencyParseResult}, indicating a successful parse.
     *
     * @param value            the value which got parsed
     * @param currencyOptional the currency optional
     * @return successful parse result
     * @author MrIvanPlays
     * @since v1.0.0
     */
    @NotNull
    public static CurrencyParseResult success(double value, Optional<Currency> currencyOptional) {
        return new CurrencyParseResult(State.SUCCESS, value, currencyOptional);
    }

    /**
     * Creates a new {@link CurrencyParseResult}, which is not indicating successful parse.
     *
     * @param state the state/reason why parsing failed
     * @return state only parse result
     * @author MrIvanPlays
     * @since v1.0.0
     */
    @NotNull
    public static CurrencyParseResult failure(@NotNull State state) {
        return new CurrencyParseResult(state, -1, null);
    }

    private final State state;
    private final OptionalDouble value;
    private final Optional<Currency> currency;

    private CurrencyParseResult(@NotNull State state, double value, Optional<Currency> currencyOptional) {
        this.state = Objects.requireNonNull(state, "state");
        this.value = value == -1 ? OptionalDouble.empty() : OptionalDouble.of(value);
        this.currency = currencyOptional != null ? currencyOptional : Optional.empty();
    }

    /**
     * Get the state of this parse result.
     *
     * @return state
     * @author MrIvanPlays
     * @since v1.0.0
     */
    @NotNull
    public State getState() {
        return state;
    }

    /**
     * Get the value parsed.
     *
     * @return value parsed, or empty optional
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public OptionalDouble getValue() {
        return value;
    }

    /**
     * Get the currency parsed.
     *
     * @return parsed currency, or empty optional
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public Optional<Currency> getCurrency() {
        return currency;
    }

}
