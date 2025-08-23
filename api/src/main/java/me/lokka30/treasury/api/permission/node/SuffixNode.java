/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node;

import org.jetbrains.annotations.NotNull;

// todo: value type??
public interface SuffixNode extends Node {

    @Override
    @NotNull
    default Node.Type type() {
        return Type.SUFFIX;
    }
}
