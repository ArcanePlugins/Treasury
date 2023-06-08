/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.hooks.placeholder;

import me.lokka30.treasury.plugin.core.hooks.PlayerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SpecificPlaceholderHook {

    @NotNull
    String prefix();

    boolean setup();

    void clear();

    @Nullable String onRequest(@Nullable PlayerData player, @NotNull String param);

}
