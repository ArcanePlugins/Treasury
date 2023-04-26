/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import com.google.common.reflect.TypeToken;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

class EventTypeTracker {

    private final Map<Class<?>, List<Class<?>>> friends = new ConcurrentHashMap<>();

    @NotNull
    public List<Class<?>> getFriendsOf(@NotNull Class<?> event) {
        List<Class<?>> computedFriends = friends.computeIfAbsent((event), (eventType) -> {
            return getEventTypes(eventType)
                    .filter(type -> type != eventType)
                    .collect(Collectors.toList());
        });
        return Collections.unmodifiableList(computedFriends);
    }

    private static <E> Stream<Class<? super E>> getEventTypes(Class<E> eventType) {
        return TypeToken
                .of(eventType)
                .getTypes()
                .rawTypes()
                .stream()
                .filter(type -> type != Object.class);
    }

}
