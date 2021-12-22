/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.response;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * A class that represents a description for a fail-case scenario when an action is attempted.
 *
 * @author creatorfromhell
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface FailureReason {

    /**
     * Creates a new {@link FailureReason} with the specified {@code description}
     *
     * @param description the description behind this {@link FailureReason fail case}
     * @return new failure reason
     * @author MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    static FailureReason of(@NotNull String description) {
        Objects.requireNonNull(description, "description");
        return () -> description;
    }

    /**
     * Gets the description behind this {@link FailureReason fail case}.
     *
     * @return The description behind this {@link FailureReason fail case}.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull String getDescription();

}
