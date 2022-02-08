/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

/**
 * Represents a priority of an {@link EventSubscriber}.
 *
 * @author MrIvanPlays
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
 */
public enum EventPriority {

    /**
     * Event call is of low importance and should be ran first, to allow
     * other subscribers to further customise the outcome.
     */
    LOW,

    /**
     * Event call is neither important nor unimportant, and may be ran
     * normally.
     */
    NORMAL,

    /**
     * Event call is of high importance and must have one of the final says
     * in what happens to the event.
     */
    HIGH
}
