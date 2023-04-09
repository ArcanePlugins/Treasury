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

public interface PermissionNode extends Node {

    @Contract(value = "-> new", pure = true)
    @NotNull
    static PermissionNode.Builder newBuilder() {
        return new PermissionNode.Builder();
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static PermissionNode.Builder newBuilder(@NotNull PermissionNode fromNode) {
        return new PermissionNode.Builder(fromNode);
    }

    @Override
    @NotNull
    default Node.Type type() {
        return Type.PERMISSION;
    }

    @NotNull TriState value();

    final class Builder {

        private String key;
        private TriState value;
        private int weight;
        private boolean inherited;
        private NodeHolder owner, inheritedFrom;
        private ContextSet contextSet;

        public Builder() {
        }

        public Builder(@NotNull PermissionNode fromNode) {
            this.key = fromNode.key();
            this.value = fromNode.value();
            this.weight = fromNode.weight();
            this.inherited = fromNode.isInherited();
            this.inheritedFrom = fromNode.inheritedFrom().orElse(null);
            this.owner = fromNode.owner().orElse(null);
            this.contextSet = fromNode.contextSet();
        }

        public Builder(@NotNull Builder other) {
            this.key = other.key;
            this.value = other.value;
            this.weight = other.weight;
            this.inherited = other.inherited;
            this.inheritedFrom = other.inheritedFrom;
            this.owner = other.owner;
            this.contextSet = other.contextSet;
        }

        @Contract("-> new")
        @NotNull
        public Builder copy() {
            return new Builder(this);
        }

        @Contract("_ -> this")
        public Builder withKey(@NotNull String key) {
            this.key = key;
            return this;
        }

        @Contract("_ -> this")
        public Builder withValue(@NotNull TriState value) {
            this.value = value;
            return this;
        }

        @Contract("_ -> this")
        public Builder withWeight(int weight) {
            this.weight = weight;
            return this;
        }

        @Contract("_ -> this")
        public Builder withOwner(@NotNull NodeHolder owner) {
            this.owner = owner;
            return this;
        }

        @Contract("_ -> this")
        public Builder withInheritedFrom(@NotNull NodeHolder inheritedFrom) {
            this.inherited = true;
            this.inheritedFrom = inheritedFrom;
            return this;
        }

        @Contract("_ -> this")
        public Builder withContextSet(@NotNull ContextSet contextSet) {
            this.contextSet = contextSet;
            return this;
        }

        @NotNull
        public PermissionNode build() {
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
