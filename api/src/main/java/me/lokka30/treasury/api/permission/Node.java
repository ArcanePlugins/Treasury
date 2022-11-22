/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission;

import org.jetbrains.annotations.NotNull;

public interface Node<Data> {

    @NotNull
    NodeType<Data> nodeType();

    @NotNull
    Context context();

    @NotNull
    String key();

    @NotNull
    Data data();
}
