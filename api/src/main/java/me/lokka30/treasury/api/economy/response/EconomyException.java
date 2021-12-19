/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.response;

import me.lokka30.treasury.api.common.response.FailureReason;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An exception for economic problems.
 *
 * <p>For performance reasons, this exception does not fill in the stack trace.
 *
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public class EconomyException extends Exception {

    private final @NotNull FailureReason reason;

    /**
     * Construct a new {@code EconomyException}.
     *
     * @param reason  the {@link FailureReason} representing the reason for failure
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public EconomyException(@NotNull FailureReason reason) {
        this(reason, reason.getDescription(), null);
    }

    /**
     * Construct a new {@code EconomyException}.
     *
     * @param reason  the {@link FailureReason} representing the reason for failure
     * @param message a more detailed description of the problem
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public EconomyException(@NotNull FailureReason reason, @NotNull String message) {
        this(reason, message, null);
    }

    /**
     * Construct a new {@code EconomyException}.
     *
     * @param reason the {@link FailureReason} representing the reason for failure
     * @param cause  the {@link Throwable} representing or causing the problem
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
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
     * @param reason  the {@link FailureReason} representing the reason for failure
     * @param message a more detailed description of the problem
     * @param cause   the {@link Throwable} representing or causing the problem
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public EconomyException(@NotNull FailureReason reason, @NotNull String message, @Nullable Throwable cause) {
        super(message, cause, true, false);
        this.reason = reason;
    }

    /**
     * Get a {@link FailureReason} representing why the failure occurred.
     *
     * @return the reason for failure
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    public @NotNull FailureReason getReason() {
        return this.reason;
    }

    /**
     * Get a more detailed description of the reason for failure.
     *
     * @return a more detailed description of the problem
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @Override
    public @NotNull String getMessage() {
        return super.getMessage();
    }

}
