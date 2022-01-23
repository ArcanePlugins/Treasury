package me.lokka30.treasury.plugin.bukkit.metrics;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature;
import me.lokka30.treasury.plugin.bukkit.BukkitTreasuryPlugin;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Nullable;

public class BukkitMetricsManager {

    Metrics metrics;

    public void load() {
        metrics = new Metrics(((BukkitTreasuryPlugin) TreasuryPlugin.getInstance()).getJavaPlugin(),
                12927);

        metrics.addCustomChart(new SimplePie("economy-provider-name", () -> {
            final RegisteredServiceProvider<EconomyProvider> economyProvider = getEconomyProviderRegistration();
            return economyProvider == null ? "None" : economyProvider.getPlugin().getName();
        }));

        metrics.addCustomChart(new SimplePie("economy-provider-supports-negative-balances", () -> {
            final RegisteredServiceProvider<EconomyProvider> economyProvider = getEconomyProviderRegistration();
            return economyProvider == null ? null :
                    Boolean.toString(economyProvider.getProvider().getSupportedOptionalEconomyApiFeatures()
                            .contains(OptionalEconomyApiFeature.NEGATIVE_BALANCES));
        }));

        metrics.addCustomChart(new SimplePie("economy-provider-supports-bukkit-transaction-events", () -> {
            final RegisteredServiceProvider<EconomyProvider> economyProvider = getEconomyProviderRegistration();
            return economyProvider == null ? null :
                    Boolean.toString(economyProvider.getProvider().getSupportedOptionalEconomyApiFeatures()
                            .contains(OptionalEconomyApiFeature.BUKKIT_TRANSACTION_EVENTS));
        }));

        metrics.addCustomChart(new SimplePie("economy-treasury-api-version", () -> {
            //noinspection deprecation
            return EconomyAPIVersion.getCurrentAPIVersion().toString();
        }));

        metrics.addCustomChart(new SimplePie("economy-provider-api-version", () -> {
            final RegisteredServiceProvider<EconomyProvider> economyProvider = getEconomyProviderRegistration();
            return economyProvider == null ? null :
                    economyProvider.getProvider().getSupportedAPIVersion().toString();
        }));

        metrics.addCustomChart(new SimplePie("plugin-update-checking-enabled", () ->
                Boolean.toString(TreasuryPlugin.getInstance().configAdapter().getSettings().checkForUpdates())));

        metrics.addCustomChart(new SimplePie("economy-provider-supports-negative-balances", () -> {
            final RegisteredServiceProvider<EconomyProvider> economyProvider = getEconomyProviderRegistration();
            if(economyProvider == null) { return null; }

            final int size = economyProvider.getProvider().getCurrencies().size();

            if(size >= 10) {
                return "10+";
            } else {
                return Integer.toString(size);
            }
        }));
    }

    @Nullable
    private RegisteredServiceProvider<EconomyProvider> getEconomyProviderRegistration() {
        return Bukkit.getServicesManager().getRegistration(EconomyProvider.class);
    }

}
