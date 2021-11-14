package me.lokka30.treasury.plugin.core;

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
    String getName();
}
