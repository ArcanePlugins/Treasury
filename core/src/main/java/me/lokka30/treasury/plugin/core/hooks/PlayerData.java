/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.hooks;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a basic data of a player
 *
 * @author MrIvanPlays
 */
public interface PlayerData {

    @Nullable
    String name();

    @Nullable
    UUID uniqueId();

    @Nullable
    String getLocale();

}
