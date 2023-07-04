/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor;

import me.lokka30.treasury.plugin.core.Platform;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a handler for determining on what server vendor we're running. This is in order
 * to optimise some stuff which can be optimised.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public final class BukkitVendor {

    private static boolean paper = false;
    private static boolean ranPaperCheck = false;
    private static boolean spigot = false;
    private static boolean ranSpigotCheck = false;
    private static boolean folia = false;
    private static boolean ranFoliaCheck = false;

    private static Platform platform;

    public static @NotNull Platform getPlatformClass() {
        if (platform == null) {
            String specificationName;
            if (isFolia()) {
                specificationName = "Folia";
            } else if (isPaper()) {
                specificationName = "Paper";
            } else if (isSpigot()) {
                specificationName = "Spigot";
            } else {
                // Definitely CraftBukkit
                specificationName = "CraftBukkit";
            }
            platform = new Platform("Bukkit", specificationName);
        }
        return platform;
    }

    /**
     * Returns whether any of the specified classpaths are currently loaded by the JVM. Useful for
     * checking for server software implementations.
     *
     * @param classpaths classpaths to check the existence of
     * @return whether any of the specified classpaths are currently loaded by the JVM
     */
    private static boolean hasAnyClasspath(@NotNull String @NotNull... classpaths) {
        for (final String classpath : classpaths) {
            try {
                Class.forName(classpath);
                return true;
            } catch (ClassNotFoundException ignored) {
            }
        }
        return false;
    }

    /**
     * Returns whether we're running spigot.
     *
     * @return spigot?
     */
    public static boolean isSpigot() {
        if (!ranSpigotCheck) {
            spigot = hasAnyClasspath("org.spigotmc.SpigotConfig");
            ranSpigotCheck = true;
        }
        return spigot;
    }

    /**
     * Returns whether we're running paper.
     *
     * @return paper?
     */
    public static boolean isPaper() {
        if (!ranPaperCheck) {
            paper = hasAnyClasspath(
                    "com.destroystokyo.paper.PaperConfig",
                    "io.papermc.paper.configuration.Configuration"
            );
            ranPaperCheck = true;
        }
        return paper;
    }

    /**
     * Returns whether we're running Folia.
     *
     * @return folia?
     */
    public static boolean isFolia() {
        if (!ranFoliaCheck) {
            folia = hasAnyClasspath("io.papermc.paper.threadedregions.RegionizedServer");
            ranFoliaCheck = true;
        }
        return folia;
    }

    private BukkitVendor() {
        throw new IllegalArgumentException("Initialization of utility-type class");
    }

}
