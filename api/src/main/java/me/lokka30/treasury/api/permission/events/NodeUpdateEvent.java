/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.events;

import me.lokka30.treasury.api.permission.node.Node;
import me.lokka30.treasury.api.permission.node.holder.NodeHolder;

public class NodeUpdateEvent extends NodeEvent {

    private final NodeHolder nodeHolder;

    public NodeUpdateEvent(NodeHolder nodeHolder, Node<?> node) {
        super(node);
        this.nodeHolder = nodeHolder;
    }

    public NodeHolder getNodeHolder() {
        return nodeHolder;
    }

}
