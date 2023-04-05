package me.lokka30.treasury.api.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

class ServerCauseTest {

    @Test
    void testGet() {
        Cause<String> server = Cause.SERVER;

        Assertions.assertEquals("Server", server.identifier(), "Server cause identifier mismatch");
    }

    @Test
    void testGet2() {
        Assertions.assertDoesNotThrow((ThrowingSupplier<Object>) () -> Cause.SERVER);
    }

}
