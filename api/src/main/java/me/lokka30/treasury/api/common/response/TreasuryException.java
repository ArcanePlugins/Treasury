/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.response;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link RuntimeException}, indicating failure, without a stacktrace.
 *
 * @author yannicklamprecht, MrIvanPlays
 * @since 1.1.0 but heavily modified in 2.0.0
 */
public class TreasuryException extends RuntimeException {

    /**
     * Create a new {@code TreasuryException}
     *
     * @param message a message, representing a reason why failure occurred
     */
    public TreasuryException(@NotNull String message) {
        super(message, null, false, false);
    }

}
