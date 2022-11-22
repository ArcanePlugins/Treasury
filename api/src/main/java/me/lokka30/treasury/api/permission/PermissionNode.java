package me.lokka30.treasury.api.permission;

import me.lokka30.treasury.api.common.misc.TriState;
import org.jetbrains.annotations.NotNull;

public interface PermissionNode extends Node<TriState> {

    @Override
    @NotNull
    default NodeType nodeType() {
        return NodeType.PERMISSION;
    }

}
