/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node;

import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.permission.context.Context;
import me.lokka30.treasury.api.permission.context.ContextSet;
import me.lokka30.treasury.api.permission.node.type.PermissionNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NodeFactory {

    @NotNull
    public PermissionNode createPermissionNode(@NotNull String permission) {
        return this.createPermissionNode(permission, ContextSet.of(Context.GLOBAL));
    }

    @NotNull
    public PermissionNode createPermissionNode(
            @NotNull String permission, @NotNull ContextSet contextSet
    ) {
        return this.createPermissionNode(permission, contextSet, (Integer) null);
    }

    @NotNull
    public PermissionNode createPermissionNode(
            @NotNull String permission, @NotNull ContextSet contextSet, @Nullable Integer weight
    ) {
        return this.createPermissionNode(permission, contextSet, weight, TriState.UNSPECIFIED);
    }

    @NotNull
    public PermissionNode createPermissionNode(
            @NotNull String permission, @NotNull ContextSet contextSet, @NotNull TriState value
    ) {
        return this.createPermissionNode(permission, contextSet, null, value);
    }

    @NotNull
    public abstract PermissionNode createPermissionNode(
            @NotNull String permission,
            @NotNull ContextSet contextSet,
            @Nullable Integer weight,
            @NotNull TriState value
    );

}
