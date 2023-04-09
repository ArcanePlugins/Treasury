/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.events;

import me.lokka30.treasury.api.permission.node.Node;
import me.lokka30.treasury.api.permission.node.holder.NodeHolder;
import org.jetbrains.annotations.NotNull;

public class NodeUpdateEvent extends NodeEvent {

    private final NodeHolder nodeHolder;

    public NodeUpdateEvent(@NotNull NodeHolder nodeHolder, @NotNull Node node) {
        super(node);
        this.nodeHolder = nodeHolder;
    }

    @NotNull
    public NodeHolder getNodeHolder() {
        return nodeHolder;
    }

}
