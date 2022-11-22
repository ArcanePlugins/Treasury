/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission;

import org.jetbrains.annotations.NotNull;

public abstract class NodeFactory {

    @NotNull
    public PermissionNode createPermissionNode(@NotNull String permission) {
        return this.createPermissionNode(permission, Context.global());
    }

    @NotNull
    public abstract PermissionNode createPermissionNode(
            @NotNull String permission, @NotNull Context context
    );

}
