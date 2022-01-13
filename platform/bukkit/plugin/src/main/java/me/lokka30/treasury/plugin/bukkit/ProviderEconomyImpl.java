/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit;

import java.util.Locale;
import java.util.Objects;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.core.ProviderEconomy;
import me.lokka30.treasury.plugin.core.RegistrarInfo;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

public class ProviderEconomyImpl implements ProviderEconomy {

    private final ServicePriority priority;
    private final RegistrarInfo registrar;
    private final EconomyProvider economyProvider;

    public ProviderEconomyImpl(
            @NotNull ServicePriority priority,
            @NotNull RegistrarInfo registrar,
            @NotNull EconomyProvider economyProvider
    ) {
        this.priority = Objects.requireNonNull(priority, "priority");
        this.registrar = Objects.requireNonNull(registrar, "registrar");
        this.economyProvider = Objects.requireNonNull(economyProvider, "economyProvider");
    }

    @Override
    public @NotNull EconomyProvider provide() {
        return economyProvider;
    }

    @Override
    public @NotNull RegistrarInfo registrar() {
        return registrar;
    }

    @Override
    public @NotNull String getPriority() {
        return priority.name().toLowerCase(Locale.ROOT);
    }

}
