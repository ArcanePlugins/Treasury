/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node;

import java.util.Optional;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.permission.context.ContextSet;
import me.lokka30.treasury.api.permission.node.holder.NodeHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class DefaultPermissionNodeImpl implements PermissionNode {

    private final String key;
    private final int weight;
    private final TriState value;
    private final boolean inherited;
    private final NodeHolder inheritedFrom, owner;
    private final ContextSet contextSet;

    DefaultPermissionNodeImpl(
            @NotNull String key,
            int weight,
            @NotNull TriState value,
            boolean inherited,
            @Nullable NodeHolder inheritedFrom,
            @Nullable NodeHolder owner,
            @Nullable ContextSet contextSet
    ) {
        this.key = key;
        this.weight = weight;
        this.value = value;
        this.inherited = inherited;
        this.inheritedFrom = inheritedFrom;
        this.owner = owner;
        this.contextSet = contextSet;
    }

    @Override
    public @NotNull String key() {
        return key;
    }

    @Override
    public int weight() {
        return weight;
    }

    @Override
    public @NotNull TriState value() {
        return value;
    }

    @Override
    public boolean isInherited() {
        return inherited;
    }

    @Override
    public @NotNull Optional<NodeHolder> inheritedFrom() {
        return Optional.ofNullable(inheritedFrom);
    }

    @Override
    public @NotNull Optional<NodeHolder> owner() {
        return Optional.ofNullable(owner);
    }

    @Override
    public @NotNull ContextSet contextSet() {
        return contextSet == null ? ContextSet.empty() : contextSet;
    }

}
