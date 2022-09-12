/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.papi.economy;

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

class EconomyTopBalancePatternTest {

    // ================================
    // basic
    @Test
    void testBase() {
        testMatcherGroups("top_balance", null, null, null, null);
    }

    @Test
    void testType() {
        testMatcherGroups("top_balance_fixed", "fixed", null, null, null);
    }

    @Test
    void testPrecision() {
        testMatcherGroups("top_balance_5dp", null, "5", null, null);
    }

    @Test
    void testRank() {
        testMatcherGroups("top_balance_3", null, null, "3", null);
    }

    @Test
    void testCurrency() {
        testMatcherGroups("top_balance_lira", null, null, null, "lira");
    }

    // ================================
    // Combination forms
    @ParameterizedTest
    @MethodSource("getCombineGroups")
    void testCombine(
            @Nullable String type,
            @Nullable String precision,
            @Nullable String rank,
            @Nullable String currency) {
        StringBuilder builder = new StringBuilder("top_balance");
        if (type != null) {
            builder.append('_').append(type);
        }
        if (precision != null) {
            builder.append('_').append(precision).append("dp");
        }
        if (rank != null) {
            builder.append('_').append(rank);
        }
        if (currency != null) {
            builder.append('_').append(currency);
        }

        testMatcherGroups(builder.toString(), type, precision, rank, currency);
    }

    public static Stream<Arguments> getCombineGroups() {
        String[] types = new String[] { null, "formatted", "fixed", "commas" };
        String[] precision = new String[] { null, "0", "10", "5" };
        String[] rank = new String[] { null, "10", "1", "2" };
        String[] currency = new String[]
                { null, "", "euro", "@%#(*^&", ";drop table users;", "5dpapa_dollar" };

        int maxIndex = types.length * precision.length * rank.length * currency.length;
        int typeIndexBase = precision.length * rank.length * currency.length;
        int precisionIndexBase = rank.length * currency.length;
        int rankIndexBase = currency.length;
        AtomicInteger index = new AtomicInteger();

        return Stream.generate(() -> {
            int currentIndex = index.getAndIncrement();
            return Arguments.of(
                    types[currentIndex / typeIndexBase],
                    precision[(currentIndex / precisionIndexBase) % precision.length],
                    rank[(currentIndex / rankIndexBase) % rank.length],
                    currency[currentIndex % currency.length]
            );
        }).limit(maxIndex);
    }

    private void testMatcherGroups(
            @NotNull String input,
            @Nullable String type,
            @Nullable String precision,
            @Nullable String rank,
            @Nullable String currency) {
        Matcher matcher = EconomyHook.TOP_BALANCE.matcher(input);
        Assertions.assertTrue(matcher.matches(), "Matcher must match input");
        Assertions.assertEquals(matcher.group("type"), type,
                () -> String.format("Incorrect type parsing: Got %s but expected %s",
                        matcher.group("type"), type));
        Assertions.assertEquals(matcher.group("precision"), precision,
                () -> String.format("Incorrect precision parsing: Got %s but expected %s",
                        matcher.group("precision"), precision));
        Assertions.assertEquals(matcher.group("rank"), rank,
                () -> String.format("Incorrect rank parsing: Got %s but expected %s",
                        matcher.group("rank"), rank));
        Assertions.assertEquals(matcher.group("currency"), currency,
                () -> String.format("Incorrect currency parsing: Got %s but expected %s",
                        matcher.group("currency"), currency));
    }

}
