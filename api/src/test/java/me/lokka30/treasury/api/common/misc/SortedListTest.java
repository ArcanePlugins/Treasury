package me.lokka30.treasury.api.common.misc;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SortedListTest {

    @Test
    void testSortedList() {
        List<Integer> list = new SortedList<>();
        list.add(3);
        list.add(1);
        list.add(2);
        list.add(5);
        list.add(4);

        Assertions.assertEquals(1, list.get(0));
        Assertions.assertEquals(2, list.get(1));
        Assertions.assertEquals(3, list.get(2));
        Assertions.assertEquals(4, list.get(3));
        Assertions.assertEquals(5, list.get(4));
    }

}
