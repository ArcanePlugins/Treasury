/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.core.config.messaging;

import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;
import java.util.Objects;

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

    private final Object a;
    private final Object b;

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
