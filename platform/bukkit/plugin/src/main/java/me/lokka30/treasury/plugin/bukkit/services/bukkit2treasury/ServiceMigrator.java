/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.services.bukkit2treasury;

import me.lokka30.treasury.api.common.services.ServicePriority;
import me.lokka30.treasury.api.common.services.ServiceProvider;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.bukkit.services.ServiceMigrationManager;
import me.lokka30.treasury.plugin.core.logging.Logger;
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
public class ServiceMigrator implements Listener {

    // TODO: This shall be removed in something like version 1.5 of the API or version 1.1.0 of
    //  the plugin

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServiceRegister(ServiceRegisterEvent event) {
        RegisteredServiceProvider<?> provider = event.getProvider();
        if (EconomyProvider.class.isAssignableFrom(provider.getService())) {
            EconomyProvider economy = (EconomyProvider) provider.getProvider();
            Plugin registrator = provider.getPlugin();
            ServiceProvider.INSTANCE.registerService(
                    EconomyProvider.class,
                    economy,
                    registrator.getName(),
                    migratePriority(provider.getPriority())
            );

            ServiceMigrationManager.INSTANCE.registerBukkit2TreasuryMigration(registrator.getName());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServiceUnregister(ServiceUnregisterEvent event) {
        RegisteredServiceProvider<?> provider = event.getProvider();
        if (EconomyProvider.class.isAssignableFrom(provider.getService())) {
            EconomyProvider economy = (EconomyProvider) provider.getProvider();
            Plugin registrator = provider.getPlugin();
            ServiceProvider.INSTANCE.unregister(EconomyProvider.class, economy);

            ServiceMigrationManager.INSTANCE.unregisterBukkit2TreasuryMigration(registrator.getName());
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
