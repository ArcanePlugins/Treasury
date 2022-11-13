/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.services.treasury2bukkit;

import me.lokka30.treasury.api.common.event.EventBus;
import me.lokka30.treasury.api.common.event.EventPriority;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.event.ServiceRegisteredEvent;
import me.lokka30.treasury.api.common.service.event.ServiceUnregisteredEvent;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.bukkit.services.ServiceMigrationManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class T2BServiceMigrator {

    private final TreasuryBukkit plugin;

    public T2BServiceMigrator(TreasuryBukkit plugin) {
        this.plugin = plugin;
    }

    public void registerListeners() {
        EventBus bus = EventBus.INSTANCE;

        bus.subscribe(bus
                .subscriptionFor(ServiceRegisteredEvent.class)
                .withPriority(EventPriority.LOW)
                .whenCalled(event -> {
                    Service<?> service = event.getService();
                    if (!EconomyProvider.class.isAssignableFrom(service.get().getClass())) {
                        return;
                    }

                    if (ServiceMigrationManager.INSTANCE.hasBeenMigratedFromBukkit(service.registrarName())) {
                        return;
                    }

                    EconomyProvider provider = (EconomyProvider) service.get();
                    ServiceMigrationManager.INSTANCE.registerTreasury2BukkitMigration(provider);
                    Bukkit.getServicesManager().register(
                            EconomyProvider.class,
                            provider,
                            plugin,
                            migratePriority(service.priority())
                    );
                })
                .completeSubscription());

        bus.subscribe(bus
                .subscriptionFor(ServiceUnregisteredEvent.class)
                .withPriority(EventPriority.LOW)
                .whenCalled(event -> {
                    Service<?> service = event.getService();
                    if (!EconomyProvider.class.isAssignableFrom(service.get().getClass())) {
                        return;
                    }

                    if (!ServiceMigrationManager.INSTANCE.hasBeenMigratedFromBukkit(service.registrarName())) {
                        return;
                    }

                    EconomyProvider provider = (EconomyProvider) service.get();
                    ServiceMigrationManager.INSTANCE.unregisterTreasury2BukkitMigration(provider);
                    Bukkit.getServicesManager().unregister(EconomyProvider.class, provider);
                })
                .completeSubscription());
    }

    private ServicePriority migratePriority(me.lokka30.treasury.api.common.service.ServicePriority priority) {
        switch (priority) {
            case LOW:
                return ServicePriority.Low;
            case HIGH:
                return ServicePriority.High;
            default:
                return ServicePriority.Normal;
        }
    }

}
