/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.services;

import java.util.HashSet;
import java.util.Set;
import me.lokka30.treasury.api.economy.EconomyProvider;

public class ServiceMigrationManager {

    public static final ServiceMigrationManager INSTANCE = new ServiceMigrationManager();

    private ServiceMigrationManager() {
    }

    private Set<String> bukkit2TreasuryMigrations = new HashSet<>();
    private Set<EconomyProvider> treasury2BukkitMigrations = new HashSet<>();

    public void shutdown() {
        bukkit2TreasuryMigrations.clear();
        treasury2BukkitMigrations.clear();
    }

    public void registerBukkit2TreasuryMigration(String registrar) {
        bukkit2TreasuryMigrations.add(registrar);
    }

    public void registerTreasury2BukkitMigration(EconomyProvider provider) {
        treasury2BukkitMigrations.add(provider);
    }

    public void unregisterBukkit2TreasuryMigration(String registrar) {
        bukkit2TreasuryMigrations.remove(registrar);
    }

    public void unregisterTreasury2BukkitMigration(EconomyProvider provider) {
        treasury2BukkitMigrations.remove(provider);
    }

    public boolean hasBeenMigratedFromBukkit(String registrar) {
        return bukkit2TreasuryMigrations.contains(registrar);
    }

    public boolean hasBeenMigratedFromTreasury(EconomyProvider provider) {
        return treasury2BukkitMigrations.contains(provider);
    }

}
