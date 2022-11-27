/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.context;

import org.jetbrains.annotations.NotNull;

public enum ContextType {
    GLOBAL,
    WORLD,
    SERVER
    ;

    private static final Context global = new Context(ContextType.GLOBAL, "");

    @NotNull
    public static Context global() {
        return global;
    }

    @NotNull
    public static Context newContext(@NotNull ContextType type, @NotNull String value) {
        if (type == GLOBAL) {
            return global;
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("value is empty");
        }
        if (value.indexOf(' ') != -1) {
            throw new IllegalArgumentException("Invalid value: value should not contain spaces!");
        }
        return new Context(type, value);
    }
}
