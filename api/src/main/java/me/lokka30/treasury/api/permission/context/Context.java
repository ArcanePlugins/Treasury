/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.context;

import org.jetbrains.annotations.NotNull;

public final class Context {

    private final ContextType type;
    private final String value;

    Context(@NotNull ContextType type, @NotNull String value) {
        this.type = type;
        this.value = value;
    }

    @NotNull
    public ContextType type() {
        return this.type;
    }

    @NotNull
    public String value() {
        return this.value;
    }

}
