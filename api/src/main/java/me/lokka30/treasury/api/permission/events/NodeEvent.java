/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.events;

import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.api.common.event.Cancellable;
import me.lokka30.treasury.api.permission.node.Node;
import org.jetbrains.annotations.NotNull;

public class NodeEvent implements Cancellable {

    private Node node;
    private Cause<?> cause;
    private boolean cancelled;

    public NodeEvent(@NotNull Node node, @NotNull Cause<?> cause) {
        this.node = node;
        this.cause = cause;
    }

    @NotNull
    public Node getNode() {
        return this.node;
    }

    public void setNode(@NotNull Node node) {
        this.node = node;
    }

    @NotNull
    public Cause<?> getCause() {
        return cause;
    }

    public void setCause(@NotNull Cause<?> cause) {
        this.cause = cause;
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
