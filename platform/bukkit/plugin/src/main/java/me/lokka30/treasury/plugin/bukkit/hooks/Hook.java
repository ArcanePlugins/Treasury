/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import org.jetbrains.annotations.NotNull;

public interface Hook {

    @NotNull
    String getPlugin();

    boolean register(@NotNull TreasuryBukkit plugin);

}
