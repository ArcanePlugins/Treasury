package me.lokka30.treasury.api.economy.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A data holder for economic failures.
 *
 * @since v1.0.0
 */
public class EconomyFailure {

    private final @NotNull FailureReason reason;
    private final @NotNull String message;
    private final @Nullable Throwable cause;

    /**
     * Construct a new {@code EconomyFailure}.
     *
     * @param reason the {@link FailureReason} representing the reason for failure
     * @param message a more detailed description of the problem
     * @since v1.0.0
     */
    public EconomyFailure(@NotNull FailureReason reason, @NotNull String message) {
        this(reason, message, null);
    }

    /**
     * Construct a new {@code EconomyFailure}.
     *
     * @param reason the {@link FailureReason} representing the reason for failure
     * @param cause the {@link Throwable} representing or causing the problem
     * @since v1.0.0
     */
    public EconomyFailure(@NotNull FailureReason reason, @NotNull Throwable cause) {
        this(
                reason,
                cause.getMessage() == null ? cause.getClass().getSimpleName() : cause.getMessage(),
                cause
        );
    }

    /**
     * Construct a new {@code EconomyFailure}.
     *
     * @param reason the {@link FailureReason} representing the reason for failure
     * @param message a more detailed description of the problem
     * @param cause the {@link Throwable} representing or causing the problem
     * @since v1.0.0
     */
    public EconomyFailure(@NotNull FailureReason reason, @NotNull String message, @Nullable Throwable cause) {
        this.reason = reason;
        this.message = message;
        this.cause = cause;
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
    public @NotNull String getMessage() {
        return this.message;
    }

    /**
     * Get the {@link Throwable} that resulted in this failure.
     *
     * <p>May be {@code null} if the failure was not caused by an exception.
     *
     * @return the {@code Throwable} causing the failure
     * @since v1.0.0
     */
    public @Nullable Throwable getCause() {
        return this.cause;
    }

}
