/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core;

import org.jetbrains.annotations.NotNull;

/**
 * The platform on which this core is running
 *
 * @author MrIvanPlays
 */
public final class Platform {

    private final String platformName, specificationName;

    public Platform(@NotNull String platformName, @NotNull String specificationName) {
        this.platformName = platformName;
        this.specificationName = specificationName;
    }

    @NotNull
    public String platformName() {
        return platformName;
    }

    @NotNull
    public String specificationName() {
        return specificationName.isEmpty() ? platformName : specificationName;
    }

    @NotNull
    public String displayName() {
        if (!specificationName.isEmpty()) {
            return platformName + " (" + specificationName + ")";
        }
        return platformName;
    }

}
