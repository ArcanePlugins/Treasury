/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.service.event;

import java.util.Objects;
import me.lokka30.treasury.api.common.service.Service;
import org.jetbrains.annotations.NotNull;

/**
 * An event, called whenever a {@link Service} has been registered
 *
 * @author MrIvanPlays
 * @since v1.1.0
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
