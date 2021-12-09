/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
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
