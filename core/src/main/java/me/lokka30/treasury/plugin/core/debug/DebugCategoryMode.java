/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.debug;

/**
 * Represents a mode, which decides how the enabled debug categories are handled.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public enum DebugCategoryMode {

    /**
     * Only the specified debug categories are going to be enabled.
     */
    WHITELIST,

    /**
     * The specified debug categories won't be enabled.
     */
    BLACKLIST
}
