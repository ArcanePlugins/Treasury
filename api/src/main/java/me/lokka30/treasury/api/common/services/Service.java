/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.services;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a service.
 *
 * @param <T> type of the data held
 * @author MrIvanPlays
 */
public final class Service<T> implements Comparable<Service<?>> {

    private final String registrator;
    private final ServicePriority priority;
    private final T service;

    public Service(
            @NotNull String registrator, @NotNull ServicePriority priority, @NotNull T service
    ) {
        this.registrator = Objects.requireNonNull(registrator, "registrator");
        this.priority = Objects.requireNonNull(priority, "priority");
        this.service = Objects.requireNonNull(service, "service");
    }

    /**
     * Returns a {@link String} identifier of who registered this service.
     *
     * @return registrator
     */
    public @NotNull String registrator() {
        return registrator;
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
    public @NotNull T get() {
        return service;
    }

    @Override
    public String toString() {
        return "Service{" + "registrator='" + registrator + '\'' + ", priority=" + priority + ", service=" + service + '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull final Service<?> other) {
        return priority.compareTo(other.priority());
    }

}
