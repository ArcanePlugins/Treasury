/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.event;

public class EventMigrationManager {

    public static final EventMigrationManager INSTANCE = new EventMigrationManager();

    private EventMigrationManager() {

    }

    private CurrentlyCalledEvent currentlyCalledEventFromBukkit;
    private CurrentlyCalledEvent currentlyCalledEventFromTreasury;

    public CurrentlyCalledEvent getCurrentlyCalledEventFromBukkit() {
        return currentlyCalledEventFromBukkit;
    }

    public void setCurrentlyCalledEventFromBukkit(CurrentlyCalledEvent currentlyCalledEventFromBukkit) {
        this.currentlyCalledEventFromBukkit = currentlyCalledEventFromBukkit;
    }

    public CurrentlyCalledEvent getCurrentlyCalledEventFromTreasury() {
        return currentlyCalledEventFromTreasury;
    }

    public void setCurrentlyCalledEventFromTreasury(CurrentlyCalledEvent currentlyCalledEventFromTreasury) {
        this.currentlyCalledEventFromTreasury = currentlyCalledEventFromTreasury;
    }

    public enum CurrentlyCalledEvent {
        NON_PLAYER,
        PLAYER
    }

}
