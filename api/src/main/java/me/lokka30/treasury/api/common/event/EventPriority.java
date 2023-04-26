/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

/**
 * Represents a priority of an {@link EventSubscriber}.
 *
 * @author MrIvanPlays
 * @since v1.1.0
 */
public enum EventPriority {

    /**
     * Event call can be considered "lowest" importance and should be run first,
     * to allow other subscribers to further customise the outcome.
     */
    LOWEST,

    /**
     * Event call is neither lowest, nor of low importance, and shall eb run in-between.
     */
    PRE_LOW,

    /**
     * Event call is of low importance and should be run as one of the first, to allow
     * other subscribers to further customise the outcome.
     */
    LOW,

    /**
     * Event call is neither low, nor of normal importance, and shall be run in-between.
     */
    POST_LOW,

    /**
     * Event call is neither important nor unimportant, and may be run
     * normally.
     */
    NORMAL,

    /**
     * Event shall be called after normal, but not with the high(est) importance.
     */
    POST_NORMAL,

    /**
     * Event call is neither of high importance, nor of normal, and shall be run in-between.
     */
    PRE_HIGH,

    /**
     * Event call is of high importance and must have one of the final says
     * in what happens to the event.
     */
    HIGH,

    /**
     * Event call is neither high, nor of highest importance, and shall be run in-between.
     */
    PRE_HIGHEST,

    /**
     * Event call is of highest importance and must have one of the final says in what happens to
     * the event.
     */
    HIGHEST
}
