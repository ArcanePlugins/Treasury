/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.response;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a specialized response of a method call,
 * containing the result of the call, or the reason the call failed.
 *
 * @param <Result> generic result type
 * @author MrIvanPlays
 * @since 2.0.0
 */
public interface Response<Result> {

    /**
     * Creates a successful response object with the specified result.
     *
     * @param result the expected result
     * @return successful response
     * @param <Result> generic result type
     * @since 2.0.0
     */
    @NotNull
    static <Result> Response<Result> success(@NotNull Result result) {
        Objects.requireNonNull(result, "result");
        return new Response<Result>() {
            @Override
            public @NotNull Result getResult() {
                return result;
            }

            @Override
            public @Nullable FailureReason getFailureReason() {
                return null;
            }
        };
    }

    /**
     * Creates a failure response object.
     *
     * @param reason the reason why the response is failure
     * @return unsuccessful response
     * @param <Result> generic result type
     * @since 2.0.0
     */
    @NotNull
    static <Result> Response<Result> failure(@NotNull FailureReason reason) {
        Objects.requireNonNull(reason, "reason");
        return new Response<Result>() {
            @Override
            public @Nullable Result getResult() {
                return null;
            }

            @Override
            public @NotNull FailureReason getFailureReason() {
                return reason;
            }
        };
    }

    /**
     * Returns whether this {@code Response} is successful.
     *
     * @return true if successful, false otherwise
     * @since 2.0.0
     */
    default boolean isSuccessful() {
        return getResult() != null && getFailureReason() == null;
    }

    /**
     * Returns the {@code Result} object of this {@code Response} if successful, null otherwise.
     *
     * @return result or null
     * @since 2.0.0
     */
    @Nullable Result getResult();

    /**
     * Returns the {@link FailureReason} if this {@code Response} was unsuccessful. Returns null
     * if successful.
     *
     * @return failure reason if response is unsuccessful, null otherwise
     * @since 2.0.0
     */
    @Nullable FailureReason getFailureReason();

}
