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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CurrencyTests {

    private static Currency euro;
    private static Currency dollar;

    @BeforeAll
    public static void registerCurrencies() {
        euro = Currency.of(
                '€',
                -1,
                1,
                (value, locale) -> "€" + value,
                "euro",
                "EUR"
        );
        dollar = Currency.of(
                '$',
                -1,
                0.884059,
                (value, locale) -> "$" + value,
                "dollar",
                "USD"
        );
        CurrencyManager.INSTANCE.registerCurrency(dollar);
        CurrencyManager.INSTANCE.registerCurrency(euro);
    }

    @Test
    public void testConvertEuroToDollar() {
        double converted = CurrencyManager.INSTANCE.convertCurrency(euro, dollar, 1);
        Assertions.assertEquals(1.1311462244035748, converted);
    }

    @Test
    public void testConvertDollarToEuro() {
        double converted = CurrencyManager.INSTANCE.convertCurrency(dollar, euro, 1);
        Assertions.assertEquals(0.884059, converted);
    }

    @Test
    public void testParsingDollar() {
        Assertions.assertEquals('$', parseCurrency("1dollar"));
        Assertions.assertEquals('$', parseCurrency("$1"));
        Assertions.assertEquals('$', parseCurrency("1$"));
        Assertions.assertEquals('$', parseCurrency("dollar1"));
    }

    @Test
    public void testParsingEuro() {
        Assertions.assertEquals('€', parseCurrency("1euro"));
        Assertions.assertEquals('€', parseCurrency("€1"));
        Assertions.assertEquals('€', parseCurrency("1€"));
        Assertions.assertEquals('€', parseCurrency("euro1"));
    }

    private Character parseCurrency(String input) {
        Currency currency = CurrencyManager.INSTANCE.parseCurrencyAndValue(input).getCurrency().orElse(null);
        return currency == null ? null : currency.getCurrencyCharacter();
    }
}
