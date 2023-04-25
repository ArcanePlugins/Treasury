/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class LogCatcherAdvanced {

    Map<LocalTime, String> logs = new ConcurrentHashMap<>();

    void log(String log) {
        this.logs.put(LocalTime.now(), log);
    }

}
