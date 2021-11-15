package me.lokka30.treasury.plugin.core;

import java.util.List;
import java.util.Objects;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.config.messaging.ColorHandler;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import org.jetbrains.annotations.NotNull;

public abstract class TreasuryPlugin {

    private static TreasuryPlugin instance;

    public static TreasuryPlugin getInstance() {
        return instance;
    }

    public static void setInstance(@NotNull TreasuryPlugin newInstance) {
        Objects.requireNonNull(newInstance, "newInstance");
        if (instance != null) {
            throw new IllegalArgumentException("Instance already set");
        }
        instance = newInstance;
    }

    @NotNull
    public abstract String getVersion();

    @NotNull
    public abstract String getDescription();

    @NotNull
    public ProviderEconomy economyProviderProvider() {
        return allProviders().get(0);
    }

    /**
     * Should give all the economy providers registered, ordered by highest priority.
     *
     * @return ordered providers list
     */
    @NotNull
    public abstract List<ProviderEconomy> allProviders();

    public abstract void registerProvider(@NotNull EconomyProvider newProvider);

    public abstract void reregisterProvider(@NotNull ProviderEconomy provider, boolean lowPriority);

    public abstract void unregisterProvider(@NotNull EconomyProvider provider);

    @NotNull
    public abstract Logger logger();

    @NotNull
    public abstract Scheduler scheduler();

    @NotNull
    public abstract ConfigAdapter configAdapter();

    @NotNull
    public abstract ColorHandler colorHandler();

    public abstract void reload();

    @NotNull
    public abstract EconomyAPIVersion getEconomyAPIVersion();
}
