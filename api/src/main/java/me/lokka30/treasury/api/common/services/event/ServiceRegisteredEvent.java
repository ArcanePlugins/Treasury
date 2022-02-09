/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.services.event;

import java.util.Objects;
import me.lokka30.treasury.api.common.services.Service;
import org.jetbrains.annotations.NotNull;

/**
 * An event, called whenever a {@link Service} has been registered
 *
 * @author MrIvanPlays
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
 */
public class ServiceRegisteredEvent {

    private final Service<?> service;

    public ServiceRegisteredEvent(@NotNull Service<?> service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    /**
     * Returns the {@link Service} which has been registered
     *
     * @return service registered
     */
    @NotNull
    public Service<?> getService() {
        return service;
    }

}
