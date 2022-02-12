/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.event;

/**
 * An interface, which can be implemented by events which should be cancellable.
 *
 * @author MrIvanPlays
 * @since v1.1.0
 */
public interface Cancellable {

    /**
     * Gets the cancellation state of this event. A cancelled event may not
     * be executed by the caller, but will still pass to other subscribers.
     *
     * @return true if this event is cancelled
     */
    boolean isCancelled();

    /**
     * Sets the cancellation state of this event. A cancelled event may not
     * be executed by the caller, but will still pass to other subscribers.
     *
     * @param cancel true if you wish to cancel this event
     */
    void setCancelled(boolean cancel);

}
