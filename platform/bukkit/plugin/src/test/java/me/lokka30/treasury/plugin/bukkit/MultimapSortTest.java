package me.lokka30.treasury.plugin.bukkit;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MultimapSortTest {

    @Test
    void testMultimapSort() {
        Multimap<String, Integer> multimap = MultimapBuilder
                .hashKeys()
                .treeSetValues(Integer::compareTo)
                .build();

        multimap.put("foo", 2);
        multimap.put("foo", 1);

        Assertions.assertEquals(1, multimap.get("foo").stream().findFirst().orElse(-1));
    }

}
