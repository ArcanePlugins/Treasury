/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node;

import java.util.Optional;
import me.lokka30.treasury.api.permission.context.ContextSet;
import me.lokka30.treasury.api.permission.node.holder.NodeHolder;
import org.jetbrains.annotations.NotNull;

public interface Node {

    @NotNull
    Type type();

    @NotNull
    String key();

    int weight();

    boolean isInherited();

    @NotNull
    Optional<NodeHolder> inheritedFrom();

    @NotNull
    Optional<NodeHolder> owner();

    @NotNull
    ContextSet contextSet();

    final class Type {

        public static final Type PERMISSION = new Type(PermissionNode.class);
        public static final Type PREFIX = new Type(PrefixNode.class);
        public static final Type SUFFIX = new Type(SuffixNode.class);

        private final Class<? extends Node> nodeClass;

        public Type(@NotNull Class<? extends Node> nodeClass) {
            this.nodeClass = nodeClass;
        }

        @NotNull
        public Class<? extends Node> getNodeClass() {
            return nodeClass;
        }

    }

}
