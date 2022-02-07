package me.lokka30.treasury.api.common.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base class for exceptions thrown in Treasury's APIs, such as the {@link me.lokka30.treasury.api.economy.response.EconomyException EconomyException}.
 *
 * <p>For performance reasons, this exception does not fill in the stack trace.
 *
 * @author yannicklamprecht
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
 */
public class TreasuryException extends Exception {

    private final @NotNull FailureReason reason;

    /**
     * Construct a new {@code TreasuryException}.
     *
     * @param reason the {@link FailureReason} representing the reason for failure
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
     */
    public TreasuryException(@NotNull FailureReason reason) {
        this(reason, reason.getDescription(), null);
    }

    /**
     * Construct a new {@code TreasuryException}.
     *
     * @param reason  the {@link FailureReason} representing the reason for failure
     * @param message a more detailed description of the problem
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
     */
    public TreasuryException(@NotNull FailureReason reason, @NotNull String message) {
        this(reason, message, null);
    }

    /**
     * Construct a new {@code TreasuryException}.
     *
     * @param reason the {@link FailureReason} representing the reason for failure
     * @param cause  the {@link Throwable} representing or causing the problem
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
     */
    public TreasuryException(@NotNull FailureReason reason, @NotNull Throwable cause) {
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
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
     */
    public TreasuryException(
            @NotNull FailureReason reason, @NotNull String message, @Nullable Throwable cause
    ) {
        super(message, cause, true, false);
        this.reason = reason;
    }

    /**
     * Get a {@link FailureReason} representing why the failure occurred.
     *
     * @return the reason for failure
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
     */
    public @NotNull FailureReason getReason() {
        return this.reason;
    }

    /**
     * Get a more detailed description of the reason for failure.
     *
     * @return a more detailed description of the problem
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
     */
    @Override
    public @NotNull String getMessage() {
        return super.getMessage();
    }

}
