/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface Node<Data> {

    @NotNull UUID getBoundTo();

    @NotNull
    NodeType nodeType();

    @NotNull
    String key();

    @NotNull
    Data data();

    void updateData(@NotNull Data data);
}
