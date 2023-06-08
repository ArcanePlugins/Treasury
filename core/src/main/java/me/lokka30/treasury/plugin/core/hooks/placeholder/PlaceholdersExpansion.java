/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.hooks.placeholder;

import java.util.List;
import me.lokka30.treasury.plugin.core.hooks.PlayerData;

public abstract class PlaceholdersExpansion {

    /**
     * Request player data for balance caching.
     *
     * @return player data
     */
    public abstract List<PlayerData> requestPlayerData();
}
