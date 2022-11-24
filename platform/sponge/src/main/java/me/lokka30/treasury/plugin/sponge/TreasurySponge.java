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
import org.spongepowered.api.event.EventListenerRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("treasury")
public class TreasurySponge {

    private final Logger logger;
    private final PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDirectory;

    @Inject
    TreasurySponge(PluginContainer container, Logger logger) {
        this.logger = logger;
        this.container = container;
    }

    private SpongeTreasuryPlugin treasuryPlugin;
    private TreasuryCommand command;

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

        CommandSources sources = new CommandSources();
        Sponge.eventManager().registerListener(EventListenerRegistration
                .builder(ServerSideConnectionEvent.Disconnect.class)
                .plugin(this.container)
                .listener(sources)
                .build());
        this.command = new TreasuryCommand(sources);

        treasuryPlugin.info("&fStart-up complete (took &b" + startupTimer.getTimer() + "ms&f)");
    }

    @Listener
    public void onShutdown(StoppingEngineEvent<Server> event) {
        QuickTimer shutdownTimer = new QuickTimer();

        // Shutdown events
        EventExecutorTrackerShutdown.shutdown();

        treasuryPlugin.info("&fShut-down complete (took &b" + shutdownTimer.getTimer() + "ms&f).");
    }

    @Listener
    public void onRegisterCommands(RegisterCommandEvent<Command.Raw> event) {
        event.register(this.container, this.command, "treasury");
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
