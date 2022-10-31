package me.lokka30.treasury.api.common.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link FailureReason} containing multiple failure reasons
 *
 * @author MrIvanPlays
 * @since 2.0.0
 */
public final class CumulatedFailureReason implements FailureReason {

    private final List<FailureReason> reasons;

    public CumulatedFailureReason() {
        this.reasons = new ArrayList<>();
    }

    public CumulatedFailureReason(@NotNull List<FailureReason> reasons) {
        this.reasons = Objects.requireNonNull(reasons, "reasons");
    }

    /**
     * Returns an unmodifiable {@link List} of all the reasons held by this {@code
     * CumulatedFailureReason}
     *
     * @return failure reasons
     */
    @NotNull
    public List<FailureReason> getReasons() {
        return Collections.unmodifiableList(this.reasons);
    }

    /**
     * Adds the specified {@link FailureReason}
     *
     * @param reason reason to add
     */
    public void addFailureReason(@NotNull FailureReason reason) {
        this.reasons.add(Objects.requireNonNull(reason, "reason"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getDescription() {
        return reasons.isEmpty()
                ? "Invalid CumulatedFailureReason"
                : reasons.get(0).getDescription();
    }

}
