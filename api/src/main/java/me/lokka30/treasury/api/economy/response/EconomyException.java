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

package me.lokka30.treasury.api.economy.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An exception for economic problems.
 *
 * <p>For performance reasons, this exception does not fill in the stack trace.
 *
 * @since v1.0.0
 */
public class EconomyException extends Exception {

    private final @NotNull FailureReason reason;

    /**
     * Construct a new {@code EconomyException}.
     *
     * @param reason the {@link FailureReason} representing the reason for failure
     * @param message a more detailed description of the problem
     * @since v1.0.0
     */
    public EconomyException(@NotNull FailureReason reason, @NotNull String message) {
        this(reason, message, null);
    }

    /**
     * Construct a new {@code EconomyException}.
     *
     * @param reason the {@link FailureReason} representing the reason for failure
     * @param cause the {@link Throwable} representing or causing the problem
     * @since v1.0.0
     */
    public EconomyException(@NotNull FailureReason reason, @NotNull Throwable cause) {
        this(
                reason,
                cause.getMessage() == null ? cause.getClass().getSimpleName() : cause.getMessage(),
                cause
        );
    }

    /**
     * Construct a new {@code EconomyException}.
     *
     * @param reason the {@link FailureReason} representing the reason for failure
     * @param message a more detailed description of the problem
     * @param cause the {@link Throwable} representing or causing the problem
     * @since v1.0.0
     */
    public EconomyException(@NotNull FailureReason reason, @NotNull String message, @Nullable Throwable cause) {
        super(message, cause, true, false);
        this.reason = reason;
    }

    /**
     * Get a {@link FailureReason} representing why the failure occurred.
     *
     * @return the reason for failure
     * @since v1.0.0
     */
    public @NotNull FailureReason getReason() {
        return this.reason;
    }

    /**
     * Get a more detailed description of the reason for failure.
     *
     * @return a more detailed description of the problem
     * @since v1.0.0
     */
    @Override
    public @NotNull String getMessage() {
        return super.getMessage();
    }

}
