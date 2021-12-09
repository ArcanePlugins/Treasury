/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core;

import org.jetbrains.annotations.NotNull;

/**
 * A class, giving information about whom registered an {@link me.lokka30.treasury.api.economy.EconomyProvider}
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public interface RegistrarInfo {

    /**
     * Get the name of the registrar
     *
     * @return name
     */
    @NotNull
    String getName();

}
