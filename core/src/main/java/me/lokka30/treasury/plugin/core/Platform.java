/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core;

import java.util.Locale;

/**
 * The platform on which this core is running
 *
 * @author MrIvanPlays
 */
public enum Platform {
    BUKKIT,
    BUNGEECORD,
    VELOCITY,
    SPONGE,
    MINESTOM
    ;

    private final String coolName;

    Platform() {
        String name = this.name();
        char first = name.charAt(0);
        name = name.substring(1).toLowerCase(Locale.ROOT);
        this.coolName = first + name;
    }

    /**
     * Returns the platform enum name, but it's better.
     * <p>Example: SPONGE's display name is Sponge, BUKKIT is Bukkit, etc.
     *
     * @return display name
     */
    public String displayName() {
        return this.coolName;
    }
}
