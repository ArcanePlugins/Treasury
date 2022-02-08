/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.ArrayList;
import java.util.List;

class LogCatcher {

    List<String> logs = new ArrayList<>();

    void log(String log) {
        this.logs.add(log);
    }

}
