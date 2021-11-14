package me.lokka30.treasury.plugin.core;

import me.lokka30.treasury.api.economy.EconomyProvider;

/**
 * A class, implemented by Treasury's plugin implementations on different platforms,
 * providing an {@link EconomyProvider} and a {@link RegistrarInfo}
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public interface ProviderEconomy {

    /**
     * Returns an economy provider
     *
     * @return provider
     */
    EconomyProvider provide();

    /**
     * Returns a registrar info
     *
     * @return registrar info
     */
    RegistrarInfo registrar();
}
