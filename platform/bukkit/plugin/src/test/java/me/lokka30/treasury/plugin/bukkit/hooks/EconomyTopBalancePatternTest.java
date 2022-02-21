/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import java.util.regex.Matcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EconomyTopBalancePatternTest {

    // ================================
    // basic
    @Test
    void testTopBalanceValid1() {
        Assertions.assertTrue(EconomyHook.TOP_BALANCE.matcher("top_balance_3_dollars").matches());
    }

    @Test
    void testTopBalanceValid2() {
        Assertions.assertTrue(EconomyHook.TOP_BALANCE.matcher("top_balance_formatted_2dp_0_dollars").matches());
    }

    @Test
    void testTopBalanceFail1() {
        Assertions.assertFalse(EconomyHook.TOP_BALANCE.matcher("top_balance_formatted_1dpapa_foo").matches());
    }

    @Test
    void testTopBalanceFail2() {
        Assertions.assertFalse(EconomyHook.TOP_BALANCE.matcher("top_balance_formatted_2dp_weeb").matches());
    }
    // ================================
    // lookahead

    @Test
    void testTopBalanceLookaheadValid1() {
        Assertions.assertTrue(EconomyHook.TOP_BALANCE.matcher("top_balance").matches());
    }

    @Test
    void testTopBalanceLookaheadValid2() {
        Assertions.assertTrue(EconomyHook.TOP_BALANCE.matcher("top_balance_").matches());
    }

}
