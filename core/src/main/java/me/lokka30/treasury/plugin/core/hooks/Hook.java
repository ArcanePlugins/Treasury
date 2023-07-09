/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.hooks;

import org.jetbrains.annotations.NotNull;

public interface Hook {

    @NotNull
    String getPlugin();

    boolean register();

    void shutdown();

}
