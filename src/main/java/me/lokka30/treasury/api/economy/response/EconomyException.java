package me.lokka30.treasury.api.economy.response;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An exception for economic problems.
 *
 * <p>Note that this exception does not fill in the stack trace automatically!
 *
 * @since 1.0.0
 */
public class EconomyException extends Exception {

    private final FailureReason reason;

    public EconomyException(@NotNull FailureReason reason, @NotNull String message) {
        this(reason, message, null);
    }

    public EconomyException(@NotNull FailureReason reason, @NotNull Throwable throwable) {
        this(reason, Objects.requireNonNull(throwable.getMessage()), throwable);
    }

    public EconomyException(@NotNull FailureReason reason, @NotNull String message, @Nullable Throwable throwable) {
        // Because we set writableStackTrace to false here, stack is not filled in
        super(message, throwable, true, false);
        this.reason = reason;
    }

    public @NotNull FailureReason getReason() {
        return reason;
    }

    @Override
    public @NotNull String getMessage() {
        return super.getMessage();
    }

}
