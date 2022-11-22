/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission;

import me.lokka30.treasury.api.common.misc.TriState;
import org.jetbrains.annotations.NotNull;

public final class NodeType<Data> {

    public static final NodeType<TriState> PERMISSION = new NodeType<>(TriState.class);

    private final Class<Data> dataClass;

    private NodeType(@NotNull Class<Data> dataClass) {
        this.dataClass = dataClass;
    }

    @NotNull
    public Class<Data> dataClass() {
        return this.dataClass;
    }

}
