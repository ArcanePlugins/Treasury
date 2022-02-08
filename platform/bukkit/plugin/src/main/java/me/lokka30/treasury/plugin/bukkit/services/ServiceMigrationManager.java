/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.services;

import java.util.HashSet;
import java.util.Set;

public class ServiceMigrationManager {

    public static final ServiceMigrationManager INSTANCE = new ServiceMigrationManager();

    private ServiceMigrationManager() {
    }

    private Set<String> bukkit2TreasuryMigrations = new HashSet<>();

    public void registerBukkit2TreasuryMigration(String registrar) {
        bukkit2TreasuryMigrations.add(registrar);
    }

    public void unregisterBukkit2TreasuryMigration(String registrar) {
        bukkit2TreasuryMigrations.remove(registrar);
    }

    public boolean hasBeenMigratedFromBukkit(String registrar) {
        return bukkit2TreasuryMigrations.contains(registrar);
    }

}
