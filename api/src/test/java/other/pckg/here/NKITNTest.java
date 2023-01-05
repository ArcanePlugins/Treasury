package other.pckg.here;

import me.lokka30.treasury.api.common.NamespacedKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NKITNTest {

    @Test
    void testCannotCreateKeyInsideTreasuryNamespace() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> NamespacedKey.of("treasury", "dolor")
        );
    }

}
