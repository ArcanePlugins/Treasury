package me.lokka30.treasury.plugin.core.hooks.placeholder.economy;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EconomyBalancePatternTest {

    @Test
    void testBase() {
        testMatcherGroups("balance", null, null, null);
    }

    @Test
    void testType() {
        testMatcherGroups("balance_commas", "commas", null, null);
    }

    @Test
    void testPrecision() {
        testMatcherGroups("balance_1dp", null, "1", null);
    }

    @Test
    void testCurrency() {
        testMatcherGroups("balance_libra", null, null, "libra");
    }

    @ParameterizedTest
    @MethodSource("getCombineGroups")
    void testCombine(
            @Nullable String type, @Nullable String precision, @Nullable String currency
    ) {
        StringBuilder builder = new StringBuilder("balance");
        if (type != null) {
            builder.append('_').append(type);
        }
        if (precision != null) {
            builder.append('_').append(precision);
        }
        if (currency != null) {
            builder.append('_').append(currency);
        }

        testMatcherGroups(builder.toString(), type, precision, currency);
    }

    public static Stream<Arguments> getCombineGroups() {
        String[] types = new String[]{null, "formatted", "fixed", "commas"};
        String[] precision = new String[]{null, "0", "10", "5"};
        String[] currency = new String[]{null, "", "euro", "@%#(*^&", ";drop table users;", "5dpapa_dollar"};

        int maxIndex = types.length * precision.length * currency.length;
        int typeIndexBase = precision.length * currency.length;
        AtomicInteger index = new AtomicInteger();

        return Stream.generate(() -> {
            int currentIndex = index.getAndIncrement();
            return Arguments.of(types[currentIndex / typeIndexBase],
                    precision[(currentIndex / maxIndex) % precision.length],
                    currency[currentIndex % currency.length]
            );
        }).limit(maxIndex);
    }

    private void testMatcherGroups(
            @NotNull String input,
            @Nullable String type,
            @Nullable String precision,
            @Nullable String currency
    ) {
        Matcher matcher = EconomyHook.BALANCE.matcher(input);
        Assertions.assertTrue(matcher.matches(), "Matcher must match input");
        Assertions.assertEquals(matcher.group("type"), type, () -> String.format(
                "Incorrect type parsing: Got %s but expected %s",
                matcher.group("type"),
                type
        ));
        Assertions.assertEquals(matcher.group("precision"), precision, () -> String.format(
                "Incorrect precision parsing: Got %s but expected %s",
                matcher.group("precision"),
                precision
        ));
        Assertions.assertEquals(matcher.group("currency"), currency, () -> String.format(
                "Incorrect currency parsing: Got %s but expected %s",
                matcher.group("currency"),
                currency
        ));
    }

}
