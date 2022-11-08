package me.lokka30.treasury.api.economy.account;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.lokka30.treasury.api.common.misc.TriState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestRetrievePermissionsMap {

    private static DummyAccountImpl account;
    private static List<UUID> randomIds;

    @BeforeAll
    static void initialize() {
        randomIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        account = new DummyAccountImpl("dummy", randomIds);
    }

    @AfterAll
    static void terminate() {
        randomIds = null;
        account = null;
    }

    @Test
    void testRetrieve() throws Exception {
        Map<UUID, Set<Map.Entry<AccountPermission, TriState>>> abominationMap = account
                .retrievePermissionsMap()
                .get()
                .getResult();

        System.out.println(abominationMap);

        for (UUID uuid : randomIds) {
            Set<Map.Entry<AccountPermission, TriState>> abominationResults = abominationMap.get(uuid);
            Map<AccountPermission, TriState> permissionsMap = account
                    .retrievePermissions(uuid)
                    .get()
                    .getResult();

            if (!equals(permissionsMap, abominationResults)) {
                Assertions.fail("Permission maps do not equal for UUID: " + uuid
                        + "\npermissionsMap: "
                        + permissionsMap
                        + "\nabominationResults: " + abominationResults);
            }
        }
    }

    boolean equals(
            Map<AccountPermission, TriState> map,
            Set<Map.Entry<AccountPermission, TriState>> entries
    ) {
        boolean ret = true;
        for (Map.Entry<AccountPermission, TriState> entry : map.entrySet()) {
            if (!entries.contains(entry)) {
                ret = false;
                break;
            }
        }
        return ret;
    }

}
