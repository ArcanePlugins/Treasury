package me.lokka30.treasury.plugin.core;

import java.util.Objects;
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
    public abstract ProviderEconomy economyProviderProvider();
}
