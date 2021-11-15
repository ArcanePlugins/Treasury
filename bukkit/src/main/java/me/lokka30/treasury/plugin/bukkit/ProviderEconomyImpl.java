package me.lokka30.treasury.plugin.bukkit;

import java.util.Locale;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.core.ProviderEconomy;
import me.lokka30.treasury.plugin.core.RegistrarInfo;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.Nullable;

public class ProviderEconomyImpl implements ProviderEconomy {

    private final ServicePriority priority;
    private final RegistrarInfo registrar;
    private final EconomyProvider economyProvider;

    public ProviderEconomyImpl(ServicePriority priority, RegistrarInfo registrar, EconomyProvider economyProvider) {
        this.priority = priority;
        this.registrar = registrar;
        this.economyProvider = economyProvider;
    }

    @Override
    public @Nullable EconomyProvider provide() {
        return economyProvider;
    }

    @Override
    public @Nullable RegistrarInfo registrar() {
        return registrar;
    }

    @Override
    public @Nullable String getPriority() {
        return priority.name().toLowerCase(Locale.ROOT);
    }
}
