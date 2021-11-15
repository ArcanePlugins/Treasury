package me.lokka30.treasury.plugin.core;

import me.lokka30.treasury.api.economy.EconomyProvider;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    EconomyProvider provide();

    /**
     * Returns a registrar info
     *
     * @return registrar info
     */
    @Nullable
    RegistrarInfo registrar();

    /**
     * Returns the priority the provider has been registered on
     *
     * @return priority
     */
    @Nullable
    String getPriority();
}
