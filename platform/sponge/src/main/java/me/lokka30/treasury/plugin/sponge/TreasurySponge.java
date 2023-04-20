/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge;

import com.google.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import me.lokka30.treasury.plugin.core.utils.UpdateChecker;
import me.lokka30.treasury.plugin.sponge.apiimpl.economy.EconomyServiceImpl;
import me.lokka30.treasury.plugin.sponge.apiimpl.economy.EconomyServiceImplProvider;
import org.apache.logging.log4j.Logger;
import org.bstats.charts.SimplePie;
import org.bstats.sponge.Metrics;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.LoadedGameEvent;
import org.spongepowered.api.event.lifecycle.ProvideServiceEvent;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("treasury")
public class TreasurySponge {

    private final Logger logger;
    private final PluginContainer container;

    private final Metrics.Factory metricsFactory;

    private final TreasuryCommand command;
    private final CommandSources sources;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDirectory;

    @Inject
    TreasurySponge(PluginContainer container, Logger logger, Metrics.Factory metricsFactory) {
        this.logger = logger;
        this.container = container;
        this.metricsFactory = metricsFactory;

        this.sources = new CommandSources();
        this.command = new TreasuryCommand(this.sources);
    }

    private SpongeTreasuryPlugin treasuryPlugin;

    @Listener
    public void onStart(StartedEngineEvent<Server> event) {
        QuickTimer startupTimer = new QuickTimer();

        if (!Files.exists(configDirectory)) {
            try {
                Files.createDirectories(configDirectory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        treasuryPlugin = new SpongeTreasuryPlugin(this);
        treasuryPlugin.loadSettings();
        treasuryPlugin.loadMessages();
        TreasuryPlugin.setInstance(treasuryPlugin);

        UpdateChecker.checkForUpdates();
        //loadMetrics(); // TODO: Something's wrong with the metrics class. Needs investigation

        treasuryPlugin.logStartupMessage(startupTimer, true);
    }

    @Listener
    public void onShutdown(StoppingEngineEvent<Server> event) {
        treasuryPlugin.shutdown(true);
    }

    @Listener
    public void onRegisterCommands(RegisterCommandEvent<Command.Parameterized> event) {
        event.register(this.container, this.command.buildCommand(), "treasury");
    }

    @Listener
    public void onLoadedGame(LoadedGameEvent event) {
        Sponge.eventManager().registerListeners(this.container,
                new CommandSources.QuitListener(this.sources)
        );
        if (treasuryPlugin.getSettings().shouldSyncEcoApi()) {
            EconomyServiceImplProvider.fulfilTreasuryProvider();
        }
    }

    @Listener
    public void provideEconomyService(ProvideServiceEvent.GameScoped<EconomyService> event) {
        if (treasuryPlugin.getSettings().shouldSyncEcoApi()) {
            event.suggest(EconomyServiceImpl::new);
        }
    }

    @Listener
    public void onReload(RefreshGameEvent event) {
        logger.info("Reload triggered");
        treasuryPlugin.reload();
        logger.info("Configurations reloaded");
    }

    private void loadMetrics() {
        Metrics metrics = metricsFactory.make(16997);

        Service<EconomyProvider> service = ServiceRegistry.INSTANCE
                .serviceFor(EconomyProvider.class)
                .orElse(null);

        EconomyProvider economyProvider = service == null ? null : service.get();
        String pluginName = service == null ? null : service.registrarName();

        metrics.addCustomChart(new SimplePie(
                "economy-provider-name",
                () -> economyProvider == null ? "None" : pluginName
        ));

        metrics.addCustomChart(new SimplePie(
                "plugin-update-checking-enabled",
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

    public Logger getLogger() {
        return this.logger;
    }

    public Path getDataDir() {
        return configDirectory;
    }

    public PluginContainer getContainer() {
        return this.container;
    }

}
