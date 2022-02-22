/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

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

class EconomyTopPlayerPatternTest {

    @Test
    void testBase() {
        testMatcherGroups("top_player", null, null);
    }

    @Test
    void testRank() {
        testMatcherGroups("top_player_1", "1", null);
    }

    @Test
    void testCurrency() {
        testMatcherGroups("top_player_libra", null, "libra");
    }

    @Test
    void testRankAndCurrency() {
        testMatcherGroups("top_player_1_libra", "1", "libra");
    }

    @ParameterizedTest
    @MethodSource("getCombineGroups")
    void testCombine(
            @Nullable String rank, @Nullable String currency
    ) {
        StringBuilder builder = new StringBuilder("top_player");
        if (rank != null) {
            builder.append('_').append(rank);
        }
        if (currency != null) {
            builder.append('_').append(currency);
        }

        testMatcherGroups(builder.toString(), rank, currency);
    }

    public static Stream<Arguments> getCombineGroups() {
        String[] rank = new String[]{null, "1", "2", "4"};
        String[] currency = new String[]{null, "", "euro", "@%#(*^&", ";drop table users;", "5dpapa_dollar"};

        int maxIndex = rank.length * currency.length;
        int rankIndexBase = currency.length;
        AtomicInteger index = new AtomicInteger();

        return Stream.generate(() -> {
            int currentIndex = index.getAndIncrement();
            return Arguments.of(rank[(currentIndex / rankIndexBase) % rank.length],
                    currency[currentIndex % currency.length]
            );
        }).limit(maxIndex);
    }

    private void testMatcherGroups(
            @NotNull String input, @Nullable String rank, @Nullable String currency
    ) {
        Matcher matcher = EconomyHook.TOP_PLAYER.matcher(input);
        Assertions.assertTrue(matcher.matches(), "Matcher must match input");
        Assertions.assertEquals(matcher.group("rank"), rank, () -> String.format(
                "Incorrect rank parsing: Got %s but expected %s",
                matcher.group("rank"),
                rank
        ));
        Assertions.assertEquals(matcher.group("currency"), currency, () -> String.format(
                "Incorrect currency parsing: Got %s but expected %s",
                matcher.group("currency"),
                currency
        ));
    }

}
