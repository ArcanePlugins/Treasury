package me.lokka30.treasury.api.common.response;

import org.jetbrains.annotations.NotNull;

/**
 * A class that represents a description for a fail-case scenario when an action is attempted.
 *
 * @author creatorfromhell
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface FailureReason {

    /**
     * Gets the description behind this {@link FailureReason fail case}.
     *
     * @return The description behind this {@link FailureReason fail case}.
     * @author creatorfromhell
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull String getDescription();
}
