/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.hooks.placeholder;

import org.jetbrains.annotations.NotNull;

public interface PlaceholdersConfig {

    /**
     * Get a string from the specific config where stuff about placeholder expansions is held.
     *
     * @param key the config key
     * @param def the default value
     * @return value
     */
    String getString(@NotNull String key, String def);


    /**
     * Get an int from the specific config where stuff about placeholder expansions is held.
     *
     * @param key the config key
     * @param def the default value
     * @return value
     */
    int getInt(@NotNull String key, int def);

    /**
     * Get a boolean from the specific config where stuff about placeholder expansions is held.
     *
     * @param key the config key
     * @param def the default value
     * @return value
     */
    boolean getBoolean(@NotNull String key, boolean def);

}
