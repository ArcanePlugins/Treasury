/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit;

import me.lokka30.treasury.plugin.core.RegistrarInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class RegistrarInfoImpl implements RegistrarInfo {

    private final Plugin plugin;

    public RegistrarInfoImpl(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return plugin.getName();
    }

}
