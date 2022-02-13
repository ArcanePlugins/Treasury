/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.service;

import java.util.Objects;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a service.
 *
 * @param <T> type of the data held
 * @author MrIvanPlays
 * @since v1.1.0
 */
public final class Service<T> implements Comparable<Service<?>>, Supplier<T> {

    private final String registrarName;
    private final ServicePriority priority;
    private final T service;

    public Service(
            @NotNull String registrarName, @NotNull ServicePriority priority, @NotNull T service
    ) {
        this.registrarName = Objects.requireNonNull(registrarName, "registrarName");
        this.priority = Objects.requireNonNull(priority, "priority");
        this.service = Objects.requireNonNull(service, "service");
    }

    /**
     * Returns a {@link String} identifier of who registered this service.
     *
     * @return registrator
     */
    public @NotNull String registrarName() {
        return registrarName;
    }

    /**
     * Returns the {@link ServicePriority} at which this service was registered.
     *
     * @return priority
     */
    public @NotNull ServicePriority priority() {
        return priority;
    }

    /**
     * Returns the held service.
     *
     * @return service
     */
    @Override
    public @NotNull T get() {
        return service;
    }

    @Override
    public String toString() {
        return "Service{" + "registrarName='" + registrarName + '\'' + ", priority=" + priority + ", service=" + service + '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull final Service<?> other) {
        return priority.compareTo(other.priority());
    }

}
