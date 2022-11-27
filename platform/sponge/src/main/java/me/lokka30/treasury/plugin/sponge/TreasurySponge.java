/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge;

import com.google.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import me.lokka30.treasury.api.common.event.EventExecutorTrackerShutdown;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.LoadedGameEvent;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("treasury")
public class TreasurySponge {

    private final Logger logger;
    private final PluginContainer container;

    private final TreasuryCommand command;
    private final CommandSources sources;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDirectory;

    @Inject
    TreasurySponge(PluginContainer container, Logger logger) {
        this.logger = logger;
        this.container = container;

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

        treasuryPlugin.info("Start-up complete (took " + startupTimer.getTimer() + "ms)");
    }

    @Listener
    public void onShutdown(StoppingEngineEvent<Server> event) {
        QuickTimer shutdownTimer = new QuickTimer();

        // Shutdown events
        EventExecutorTrackerShutdown.shutdown();

        treasuryPlugin.info("Shut-down complete (took " + shutdownTimer.getTimer() + "ms).");
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
    }

    @Listener
    public void onReload(RefreshGameEvent event) {
        logger.info("Reload triggered");
        treasuryPlugin.reload();
        logger.info("Configurations reloaded");
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
