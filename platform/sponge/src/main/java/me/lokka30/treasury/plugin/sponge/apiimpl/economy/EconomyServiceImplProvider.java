/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import me.lokka30.treasury.api.common.event.EventBus;
import me.lokka30.treasury.api.common.event.EventPriority;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.common.service.event.ServiceRegisteredEvent;
import me.lokka30.treasury.api.common.service.event.ServiceUnregisteredEvent;
import me.lokka30.treasury.api.economy.EconomyProvider;

public class EconomyServiceImplProvider {
    public static final AtomicReference<EconomyProvider> PROVIDER = new AtomicReference<>();

    public static void fulfilTreasuryProvider() {
        EventBus bus = EventBus.INSTANCE;
        bus.subscribe(
            bus.subscriptionFor(ServiceRegisteredEvent.class)
                    .withPriority(EventPriority.LOW)
                    .whenCalled(event -> {
                        if (event.getService().get() instanceof EconomyProvider) {
                            return;
                        }
                        handleServiceChange();
                    })
                    .completeSubscription()
        );
        bus.subscribe(
                bus.subscriptionFor(ServiceUnregisteredEvent.class)
                        .withPriority(EventPriority.LOW)
                        .whenCalled(event -> {
                            if (event.getService().get() instanceof EconomyProvider) {
                                return;
                            }
                            handleServiceChange();
                        })
                        .completeSubscription()
        );
    }

    private static void handleServiceChange() {
        Optional<Service<EconomyProvider>> serviceOpt = ServiceRegistry.INSTANCE.serviceFor(
                EconomyProvider.class);
        if (serviceOpt.isPresent()) {
            PROVIDER.set(serviceOpt.get().get());
        } else {
            PROVIDER.set(null);
        }
    }
}
