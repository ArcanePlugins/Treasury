/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bungeecord;

import me.lokka30.treasury.api.common.event.EventExecutorTrackerShutdown;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import me.lokka30.treasury.plugin.core.utils.UpdateChecker;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;
import org.bstats.charts.SimplePie;

public class TreasuryBungee extends Plugin {

    private BungeeTreasuryPlugin treasuryPlugin;

    // onLoad instead of onEnable because in bungee you can't specify when your plugin loads
    // (except of course, if you have dependencies, then your plugin will load after the
    // dependencies)
    @Override
    public void onLoad() {
        final QuickTimer startupTimer = new QuickTimer();

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        treasuryPlugin = new BungeeTreasuryPlugin(this);
        TreasuryPlugin.setInstance(treasuryPlugin);
        treasuryPlugin.loadMessages();
        treasuryPlugin.loadSettings();
        TreasuryCommand.register(this);

        UpdateChecker.checkForUpdates();
        loadMetrics();

        treasuryPlugin.info("&fStart-up complete (took &b" + startupTimer.getTimer() + "ms&f)");
    }

    private void loadMetrics() {
        Metrics metrics = new Metrics(this, 14601);

        Service<EconomyProvider> service = ServiceRegistry.INSTANCE
                .serviceFor(EconomyProvider.class)
                .orElse(null);

        EconomyProvider economyProvider = service == null ? null : service.get();
        String pluginName = service == null ? null : service.registrarName();

        metrics.addCustomChart(new SimplePie("economy-provider-name",
                () -> economyProvider == null ? "None" : pluginName
        ));

        metrics.addCustomChart(new SimplePie("economy-provider-supports-negative-balances",
                () -> economyProvider == null
                        ? null
                        : Boolean.toString(economyProvider
                                .getSupportedOptionalEconomyApiFeatures()
                                .contains(OptionalEconomyApiFeature.NEGATIVE_BALANCES))
        ));

        metrics.addCustomChart(new SimplePie(
                // unfortunately bStats truncates the length of the id, so the 's' character
                // on the end had to be removed.
                "economy-provider-supports-transaction-events",
                () -> economyProvider == null
                        ? null
                        : Boolean.toString(economyProvider
                                .getSupportedOptionalEconomyApiFeatures()
                                .contains(OptionalEconomyApiFeature.TRANSACTION_EVENTS))
        ));

        metrics.addCustomChart(new SimplePie("plugin-update-checking-enabled",
                () -> Boolean.toString(treasuryPlugin
                        .configAdapter()
                        .getSettings()
                        .checkForUpdates())
        ));

        metrics.addCustomChart(new SimplePie("economy-provider-currencies", () -> {
            if (economyProvider == null) {
                return null;
            }

            final int size = economyProvider.getCurrencies().size();

            if (size >= 10) {
                return "10+";
            } else {
                return Integer.toString(size);
            }
        }));
    }

    @Override
    public void onDisable() {
        final QuickTimer shutdownTimer = new QuickTimer();

        // make sure to clean up even though we don't have service migration from other service
        // manager to ours as bukkit impl does. that's not really necessary, but it doesn't hurt
        // to be here
        ServiceRegistry.INSTANCE.unregisterAll("Treasury");

        // Shutdown events
        EventExecutorTrackerShutdown.shutdown();

        treasuryPlugin.info("&fShut-down complete (took &b" + shutdownTimer.getTimer() + "ms&f).");
    }

}
