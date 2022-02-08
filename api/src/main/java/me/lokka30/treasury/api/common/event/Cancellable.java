/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancel);

}
