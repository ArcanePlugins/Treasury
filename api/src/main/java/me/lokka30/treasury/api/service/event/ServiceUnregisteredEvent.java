/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.service.event;

import java.util.Objects;
import me.lokka30.treasury.api.service.Service;
import org.jetbrains.annotations.NotNull;

/**
 * An event, called whenever a {@link Service} has been unregistered
 *
 * @author MrIvanPlays
 * @since v1.1.0
 */
public class ServiceUnregisteredEvent {

    private final Service<?> service;

    public ServiceUnregisteredEvent(@NotNull Service<?> service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    /**
     * Returns the {@link Service} which has been unregistered.
     *
     * @return service unregistered
     */
    @NotNull
    public Service<?> getService() {
        return service;
    }

}
