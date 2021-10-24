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

package me.lokka30.treasury.api.economy.conversion;

import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;

/**
 * @author lokka30, Articdive, MrNemo64
 * @since v1.0.0
 * TODO Describe this class.
 * Thank you to Articdive for helping me design this class. <a href="https://github.com/Articdive">Articdive</a>
 * @see CurrencyConversion
 * @see ConversionPriority
 */
@SuppressWarnings("unused")
public class CurrencyConverter {

    @NotNull private final HashSet<CurrencyConversion> conversions = new HashSet<>();

    @Nullable
    private CurrencyConversion getConversion(int hashCode) {
        for(CurrencyConversion conversion : conversions) {
            if(conversion.hashCode == hashCode) {
                return conversion;
            }
        }

        return null;
    }

    private int getHashCodeOfCurrencies(@NotNull final Currency fromCurrency, @NotNull final Currency toCurrency) {
        return Objects.hash(fromCurrency.getCurrencyName(), toCurrency.getCurrencyName());
    }

    public boolean hasConversion(@NotNull final Currency fromCurrency, @NotNull final Currency toCurrency) {
        return getConversion(getHashCodeOfCurrencies(fromCurrency, toCurrency)) != null;
    }

    public void registerConversion(@NotNull final Currency fromCurrency, @NotNull final Currency toCurrency, @NotNull final ConversionPriority priority, final double value) {
        final int hashCode = getHashCodeOfCurrencies(fromCurrency, toCurrency);
        CurrencyConversion conversion = getConversion(hashCode);

        // check if there is already a conversion for the currencies
        if(conversion != null) {

            // if the existing conversion has a higher priority, skip
            if(conversion.priority.weight > priority.weight) return;

            conversions.remove(conversion);

        }

        conversions.add(new CurrencyConversion(hashCode, priority, value));
    }

    public double convert(@NotNull final CurrencyConversion conversion, final double value) {
        return conversion.value * value;
    }
}
