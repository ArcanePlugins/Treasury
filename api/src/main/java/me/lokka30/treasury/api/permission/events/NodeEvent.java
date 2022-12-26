/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.events;

import me.lokka30.treasury.api.common.event.Cancellable;
import me.lokka30.treasury.api.permission.node.Node;

public class NodeEvent implements Cancellable {

    private Node<?> node;
    private boolean cancelled;

    public NodeEvent(Node<?> node) {
        this.node = node;
    }

    public Node<?> getNode() {
        return this.node;
    }

    public void setNode(Node<?> node) {
        this.node = node;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

}
