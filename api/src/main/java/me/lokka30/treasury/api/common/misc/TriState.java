/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an enum, containing 3 enum values, representing states, hence the name "TriState".
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public enum TriState {
    TRUE(true),
    FALSE(false),
    UNSPECIFIED(null);

    /**
     * Returns a {@link TriState} from the specified {@link Boolean} value, which may be null.
     *
     * @param bool the boolean value to create TriState from
     * @return TriState value
     */
    @NotNull
    public static TriState fromBoolean(@Nullable Boolean bool) {
        return bool == null ? UNSPECIFIED : (bool ? TRUE : FALSE);
    }

    private final Boolean asBoolean;

    TriState(@Nullable Boolean asBoolean) {
        this.asBoolean = asBoolean;
    }

    /**
     * Returns this value as a {@code boolean}. If this is a {@link #UNSPECIFIED}, this will return null.
     *
     * @return boolean object or null
     */
    @Nullable
    public Boolean asBoolean() {
        return asBoolean;
    }
}
