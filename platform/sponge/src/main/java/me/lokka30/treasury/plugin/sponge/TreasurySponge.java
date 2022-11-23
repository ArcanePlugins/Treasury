package me.lokka30.treasury.plugin.sponge;

import com.google.inject.Inject;
import java.nio.file.Path;
import me.lokka30.treasury.api.common.event.EventExecutorTrackerShutdown;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("treasury")
public class TreasurySponge {

    private final Logger logger;
    private final PluginContainer container;

    @Inject
    TreasurySponge(PluginContainer container, Logger logger) {
        this.logger = logger;
        this.container = container;
    }

    private SpongeTreasuryPlugin treasuryPlugin;

    @Listener
    public void onStart(StartedEngineEvent<Server> event) {
        QuickTimer startupTimer = new QuickTimer();

        treasuryPlugin = new SpongeTreasuryPlugin(this);
        treasuryPlugin.loadSettings();
        treasuryPlugin.loadMessages();
        TreasuryPlugin.setInstance(treasuryPlugin);

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
    public void onRegisterCommands(RegisterCommandEvent<Command.Parameterized> event) {
        Parameter.Value<String> nameParam = Parameter.string().key("name").build();
        // TODO
        event.register(
                this.container,
                Command.builder()
                        .addParameter(nameParam)
                        .permission("treasury.command.treasury")
                        .executor(ctx -> {

                            return CommandResult.success();
                        })
                        .build(), "treasury"
        );
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Path getDataDir() {
        return null; // TODO
    }

    public PluginContainer getContainer() {
        return this.container;
    }

}
