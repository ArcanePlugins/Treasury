package me.lokka30.treasury.api.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NamespacedKeyTest {

    @Test
    void testSpecialTreasuryNamespace() {
        NamespacedKey key = NamespacedKey.of("treasury", "foo");

        Assertions.assertEquals("treasury:foo", key.toString());
    }

    @Test
    void testOtherNamespace() {
        NamespacedKey key = NamespacedKey.of("foo", "bar");

        Assertions.assertEquals("foo:bar", key.toString());
    }

    @Test
    void testFromStringLegal() {
        NamespacedKey fromString = NamespacedKey.fromString("bar:baz");

        Assertions.assertEquals("bar", fromString.getNamespace());
        Assertions.assertEquals("baz", fromString.getKey());
    }

    @Test
    void testFromStringIllegal() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> NamespacedKey.fromString("loremipsum")
        );
    }

}
