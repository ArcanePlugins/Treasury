/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node;

import java.util.Objects;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.permission.context.ContextSet;
import me.lokka30.treasury.api.permission.node.holder.NodeHolder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a permission {@link Node node}. A {@code PermissionNode} is exactly what the name
 * says - an indication whether a {@link NodeHolder node holder} has a permit to do something (e
 * .g. execute a command).
 *
 * @author <a href="mailto:ivan@mrivanplays.com">Ivan Pekov</a>
 */
public interface PermissionNode extends Node {

    /**
     * Create a new {@code PermissionNode} builder.
     *
     * @return new permission node builder
     */
    @Contract(value = "-> new", pure = true)
    @NotNull
    static PermissionNode.PermissionNodeBuilder newBuilder() {
        return new PermissionNode.PermissionNodeBuilder();
    }

    /**
     * Create a new {@code PermissionNode} builder.
     *
     * @param fromNode a permission node to create the builder from.
     * @return new permission node builder
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static PermissionNode.PermissionNodeBuilder newBuilder(@NotNull PermissionNode fromNode) {
        return new PermissionNode.PermissionNodeBuilder(fromNode);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    @NotNull
    default Node.Type type() {
        return Type.PERMISSION;
    }

    /**
     * Returns the value, held by this {@code PermissionNode}, represented by a {@link TriState
     * tri-state}.
     *
     * @return value
     * @see TriState
     */
    @NotNull TriState value();

    final class PermissionNodeBuilder extends Builder<PermissionNode, TriState> {

        public PermissionNodeBuilder() {
            super();
        }

        public PermissionNodeBuilder(@NotNull PermissionNode other) {
            super(other);
        }

        public PermissionNodeBuilder(@NotNull PermissionNodeBuilder other) {
            super(other);
        }

        @Override
        public @NotNull Builder<PermissionNode, TriState> copy() {
            return new PermissionNodeBuilder(this);
        }

        @Override
        public @NotNull PermissionNode build() {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(value, "value");
            return new DefaultPermissionNodeImpl(
                    key,
                    weight,
                    value,
                    inherited,
                    inheritedFrom,
                    owner,
                    contextSet
            );
        }

    }

}
