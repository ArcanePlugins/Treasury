/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.event;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import me.lokka30.treasury.api.common.event.EventBus;

public class EventCallerShutdown {

    public static void shutdown() {
        try {
            Field mapField = EventBus.class.getDeclaredField("events");
            mapField.setAccessible(true);
            Map map = (Map) mapField.get(EventBus.INSTANCE);

            Collection eventCallers = map.values();
            for (Object eventCaller : eventCallers) {
                Method method = eventCaller.getClass().getDeclaredMethod("shutdown");
                method.setAccessible(true);
                method.invoke(eventCaller);
            }
        } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
