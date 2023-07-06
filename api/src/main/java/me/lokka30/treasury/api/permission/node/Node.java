/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node;

import java.util.Optional;
import me.lokka30.treasury.api.permission.context.ContextSet;
import me.lokka30.treasury.api.permission.node.holder.NodeHolder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A {@code Node} represents an object, holding specific information, which can be classified
 * under so-called "permissions".
 *
 * @author <a href="mailto:ivan@mrivanplays.com">Ivan Pekov</a>
 */
public interface Node {

    /**
     * Returns the {@link Type} of the node.
     *
     * @return node type
     */
    @NotNull Type type();

    /**
     * Returns the {@link String string} key representation of this node
     *
     * @return key
     */
    @NotNull String key();

    /**
     * Returns the weight of this node. A weight represents how effective is this node compared
     * to others.
     * <p>
     * For an example, lets say a {@link NodeHolder node holder} has a node with weight 1 that
     * permits the usage of the "/give" command. That same node holder also has a node with
     * weight 2 that disallows the usage of the "/give" command. The node with the biggest weight
     * is to be applied, hence that specific node holder should <b>NOT</b> be able to use the
     * "/give" command.
     *
     * @return node weight
     */
    int weight();

    /**
     * Returns whether this node is inherited.
     * <p>
     * By "inherited" is meant that if that node is retrieved from a {@link NodeHolder node holder}
     * instance (e.g. {@link #owner()} {@link Optional#isPresent()} returns {@code true}), does
     * that node holder get this node from a parent node holder (say a parent node holder can be
     * a {@link me.lokka30.treasury.api.permission.node.holder.NodeHolderGroup group node holder}).
     *
     * @return is the node inherited
     */
    boolean isInherited();

    /**
     * Returns the {@link NodeHolder node holder} from which this node is inherited from.
     *
     * @return node holder owner of this node if present, {@link Optional#empty()} otherwise
     */
    @NotNull Optional<NodeHolder> inheritedFrom();

    /**
     * Returns the {@link NodeHolder node holder} owner (origin) of that node.
     *
     * @return owner if present, {@link Optional#empty()} otherwise
     */
    @NotNull Optional<NodeHolder> owner();

    /**
     * Returns the {@link ContextSet context set}, for which if matched this node applies.
     *
     * @return context set
     */
    @NotNull ContextSet contextSet();

    /**
     * Represents a {@link Node} type.
     * <p>
     * This class can be extended to allow for more robust type implementations.
     *
     * @author <a href="mailto:ivan@mrivanplays.com">Ivan Pekov</a>
     */
    class Type { // not final to allow custom nodes :)

        /**
         * Returns the {@link PermissionNode} type.
         */
        public static final Type PERMISSION = new Type(PermissionNode.class);

        /**
         * Returns the {@link PrefixNode} type.
         */
        public static final Type PREFIX = new Type(PrefixNode.class);

        /**
         * Returns the {@link SuffixNode} type.
         */
        public static final Type SUFFIX = new Type(SuffixNode.class);

        private final Class<? extends Node> nodeClass;

        /**
         * Construct a new type.
         *
         * @param nodeClass the node class to which this type applies to.
         */
        public Type(@NotNull Class<? extends Node> nodeClass) {
            this.nodeClass = nodeClass;
        }

        /**
         * Returns the {@link Class node class} this type applies to.
         *
         * @return node class
         */
        @NotNull
        public Class<? extends Node> getNodeClass() {
            return nodeClass;
        }

    }

    abstract class Builder<NodeType extends Node, ValueType> {

        protected String key;
        protected ValueType value;
        protected int weight;
        protected boolean inherited;
        protected NodeHolder owner, inheritedFrom;
        protected ContextSet contextSet;

        public Builder() {
        }

        public Builder(@NotNull NodeType fromNode) {
            this.key = fromNode.key();
            this.weight = fromNode.weight();
            this.inherited = fromNode.isInherited();
            this.inheritedFrom = fromNode.inheritedFrom().orElse(null);
            this.owner = fromNode.owner().orElse(null);
            this.contextSet = fromNode.contextSet();
        }

        public Builder(@NotNull Builder<NodeType, ValueType> other) {
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
        public abstract Builder<NodeType, ValueType> copy();

        @Contract("_ -> this")
        public Builder<NodeType, ValueType> withKey(@NotNull String key) {
            this.key = key;
            return this;
        }

        @Contract("_ -> this")
        public Builder<NodeType, ValueType> withValue(@NotNull ValueType value) {
            this.value = value;
            return this;
        }

        @Contract("_ -> this")
        public Builder<NodeType, ValueType> withWeight(int weight) {
            this.weight = weight;
            return this;
        }

        @Contract("_ -> this")
        public Builder<NodeType, ValueType> withOwner(@NotNull NodeHolder owner) {
            this.owner = owner;
            return this;
        }

        @Contract("_ -> this")
        public Builder<NodeType, ValueType> withInheritedFrom(@NotNull NodeHolder inheritedFrom) {
            this.inherited = true;
            this.inheritedFrom = inheritedFrom;
            return this;
        }

        @Contract("_ -> this")
        public Builder<NodeType, ValueType> withContextSet(@NotNull ContextSet contextSet) {
            this.contextSet = contextSet;
            return this;
        }

        @NotNull
        public abstract NodeType build();
    }

}
