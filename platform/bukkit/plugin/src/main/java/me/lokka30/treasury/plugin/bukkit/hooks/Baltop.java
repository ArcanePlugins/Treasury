/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Baltop {

    private final boolean enabled;
    private final int topSize, taskDelay;

    private Map<Integer, Map.Entry<String, Integer>> baltop;

    public Baltop(boolean enabled, int topSize, int taskDelay) {
        this.enabled = enabled;
        this.topSize = topSize;
        this.taskDelay = taskDelay;
        this.baltop = new ConcurrentHashMap<>();
    }



}
