/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import com.google.common.reflect.TypeToken;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class EventTypeTracker {

    private Map<Class<?>, List<Class<?>>> friends = new HashMap<>();

    public List<Class<?>> getFriendsOf(Class<?> eventType) {
        if (friends.containsKey(eventType)) {
            return Collections.unmodifiableList(friends.get(eventType));
        }

        List<Class<?>> types = getEventTypes(eventType);
        friends.put(
                eventType,
                types.stream().filter(type -> type != eventType).collect(Collectors.toList())
        );

        return friends.get(eventType);
    }

    private static List<Class<?>> getEventTypes(Class<?> eventType) {
        return TypeToken
                .of(eventType)
                .getTypes()
                .rawTypes()
                .stream()
                .filter(type -> type != Object.class)
                .collect(Collectors.toList());
    }

}
