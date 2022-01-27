/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a service provider.
 * <p>A service provider is a provider of services. A service is something like the
 * {@link me.lokka30.treasury.api.economy.EconomyProvider}
 *
 * @author MrIvanPlays
 */
public enum ServiceProvider {
    INSTANCE;

    private Map<Class<?>, Set<Service<?>>> servicesMap = new ConcurrentHashMap<>();

    /**
     * Register a provider of a service.
     *
     * @param clazz       service class
     * @param service     service to register
     * @param registrator who registers this provider
     * @param priority    priority of the service
     * @param <T>         provider
     */
    public <T> void registerService(
            @NotNull Class<T> clazz,
            @NotNull T service,
            @NotNull String registrator,
            @NotNull ServicePriority priority
    ) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(service, "service");
        Objects.requireNonNull(registrator, "registrator");
        Objects.requireNonNull(priority, "priority");
        servicesMap.computeIfAbsent(clazz, k -> new TreeSet<>()).add(new Service<>(registrator,
                priority,
                service
        ));

    }

    /**
     * Unregister all the providers registered by a particular registrator
     *
     * @param registrator the registrator
     */
    public void unregisterAll(@NotNull String registrator) {
        Objects.requireNonNull(registrator, "registrator");
        Iterator<Map.Entry<Class<?>, Set<Service<?>>>> iterator = servicesMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class<?>, Set<Service<?>>> entry = iterator.next();
            entry.getValue().removeIf(service -> service
                    .registrator()
                    .equalsIgnoreCase(registrator));

            if (entry.getValue().isEmpty()) {
                iterator.remove();
            }
        }
    }

    /**
     * Unregister a particular service.
     *
     * @param clazz   service clazz
     * @param service service
     */
    public void unregister(@NotNull Class<?> clazz, @NotNull Object service) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(service, "service");

        Iterator<Map.Entry<Class<?>, Set<Service<?>>>> iterator = servicesMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class<?>, Set<Service<?>>> entry = iterator.next();
            if (entry.getKey() != clazz) {
                continue;
            }

            entry.getValue().removeIf(s -> Objects.equals(s.get(), service));
            if (entry.getValue().isEmpty()) {
                iterator.remove();
            }
        }
    }

    /**
     * Returns whether the specified service class has a registration in this service provider.
     *
     * @param clazz class to check
     * @return boolean value
     */
    public boolean hasRegistration(@NotNull Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return servicesMap.containsKey(clazz);
    }

    /**
     * Queries for a service with the specified {@link Class}
     *
     * @param clazz the class to search a service for
     * @param <T>   service type
     * @return service or an empty optional if not present
     */
    public <T> Optional<Service<T>> serviceFor(@NotNull Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");

        Set<Service<?>> services = servicesMap.get(clazz);

        return services == null || services.isEmpty()
                ? Optional.empty()
                : services.stream().findFirst().map(s -> (Service<T>) s);
    }

    /**
     * Returns a {@link Set} copy of all the services found with the provided {@link Class}. If
     * there are no services, it returns an empty set.
     *
     * @param clazz class to find the services of
     * @param <T>   service type
     * @return services
     */
    public <T> Set<Service<T>> allServicesFor(@NotNull Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");

        Set<Service<?>> services = servicesMap.get(clazz);
        if (services == null) {
            return Collections.emptySet();
        }

        Set<Service<T>> ret = new HashSet<>();
        for (Service<?> service : services) {
            ret.add((Service<T>) service);
        }

        return ret;
    }


}
