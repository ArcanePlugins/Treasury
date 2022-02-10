/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.services.bukkit2treasury;

import me.lokka30.treasury.api.common.services.ServicePriority;
import me.lokka30.treasury.api.common.services.ServiceProvider;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.bukkit.services.ServiceMigrationManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * A listener to all Bukkit's service events in order to migrate economy providers to our own
 * service provider.
 *
 * @author MrIvanPlays
 */
public class B2TServiceMigrator implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServiceRegister(ServiceRegisterEvent event) {
        RegisteredServiceProvider<?> provider = event.getProvider();
        if (EconomyProvider.class.isAssignableFrom(provider.getService())) {
            EconomyProvider economy = (EconomyProvider) provider.getProvider();
            if (ServiceMigrationManager.INSTANCE.hasBeenMigratedFromTreasury(economy)) {
                return;
            }
            Plugin registrator = provider.getPlugin();
            ServiceMigrationManager.INSTANCE.registerBukkit2TreasuryMigration(registrator.getName());
            ServiceProvider.INSTANCE.registerService(
                    EconomyProvider.class,
                    economy,
                    registrator.getName(),
                    migratePriority(provider.getPriority())
            );
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServiceUnregister(ServiceUnregisterEvent event) {
        RegisteredServiceProvider<?> provider = event.getProvider();
        if (EconomyProvider.class.isAssignableFrom(provider.getService())) {
            EconomyProvider economy = (EconomyProvider) provider.getProvider();
            if (!ServiceMigrationManager.INSTANCE.hasBeenMigratedFromTreasury(economy)) {
                return;
            }
            Plugin registrator = provider.getPlugin();
            ServiceMigrationManager.INSTANCE.unregisterBukkit2TreasuryMigration(registrator.getName());
            ServiceProvider.INSTANCE.unregister(EconomyProvider.class, economy);
        }
    }

    private ServicePriority migratePriority(org.bukkit.plugin.ServicePriority bukkit) {
        switch (bukkit) {
            case High:
            case Highest:
                return ServicePriority.HIGH;
            case Low:
            case Lowest:
                return ServicePriority.LOW;
            default:
                return ServicePriority.NORMAL;
        }
    }

}
