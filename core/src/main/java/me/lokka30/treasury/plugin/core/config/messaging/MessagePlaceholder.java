/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.config.messaging;

import java.beans.ConstructorProperties;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a message placeholder
 *
 * @since v1.0.0
 */
public final class MessagePlaceholder {

    /**
     * Creates a new {@link MessagePlaceholder}
     *
     * @param a to replace
     * @param b replacement
     * @return message placeholder instance
     */
    public static MessagePlaceholder placeholder(@NotNull Object a, @NotNull Object b) {
        return new MessagePlaceholder(a, b);
    }

    private Object a;
    private Object b;

    @ConstructorProperties({"a", "b"})
    private MessagePlaceholder(@NotNull Object a, @NotNull Object b) {
        this.a = Objects.requireNonNull(a, "null toReplace");
        this.b = Objects.requireNonNull(b, "null replacement");
    }

    public Object getToReplace() {
        return a;
    }

    public Object getReplacement() {
        return b;
    }

}
