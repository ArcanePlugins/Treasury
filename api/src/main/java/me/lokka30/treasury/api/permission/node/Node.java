/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node;

import me.lokka30.treasury.api.permission.context.Context;
import me.lokka30.treasury.api.permission.node.type.NodeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Node<Data> {

    @NotNull NodeType<Data> nodeType();

    @NotNull Context context();

    @Nullable Integer weight();

    @NotNull String key();

    @NotNull Data data();

    @NotNull Node<Data> copyWithNewData(
            @NotNull Context context, @Nullable Integer weight, @NotNull Data data
    );

}
