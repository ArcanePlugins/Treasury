package me.lokka30.treasury.plugin.sponge;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import me.lokka30.treasury.plugin.core.Platform;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import me.lokka30.treasury.plugin.core.config.settings.Settings;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import me.lokka30.treasury.plugin.core.utils.PluginVersion;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

public class SpongeTreasuryPlugin extends TreasuryPlugin implements Logger, Scheduler,
        ConfigAdapter {

    private final TreasurySponge plugin;
    private final PluginVersion version;

    private Messages messages;
    private Settings settings;

    private final File messagesFile;
    private final File settingsFile;

    public SpongeTreasuryPlugin(TreasurySponge plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        messagesFile = new File(plugin.getDataDir().toFile(), "messages.yml");
        settingsFile = new File(plugin.getDataDir().toFile(), "settings.yml");

        // todo: somehow parse back the container#metadata#version back into a string
        this.version = new PluginVersion("", this);
    }

    @Override
    public @NotNull PluginVersion getVersion() {
        return this.version;
    }

    @Override
    public @NotNull Platform platform() {
        return Platform.SPONGE;
    }

    @Override
    public @NotNull Path pluginsFolder() {
        return null; // TODO
    }

    @Override
    public @NotNull Logger logger() {
        return this;
    }

    @Override
    public @NotNull Scheduler scheduler() {
        return this;
    }

    @Override
    public @NotNull ConfigAdapter configAdapter() {
        return this;
    }

    @Override
    public void reload() {
        this.loadMessages();
        this.loadSettings();
    }

    public void loadMessages() {
        messages = Messages.load(messagesFile);
    }

    public void loadSettings() {
        settings = Settings.load(settingsFile);
    }

    @Override
    public @NotNull Messages getMessages() {
        return this.messages;
    }

    @Override
    public @NotNull Settings getSettings() {
        return this.settings;
    }

    @Override
    public void info(final String message) {
        plugin.getLogger().info(this.color(message));
    }

    @Override
    public void warn(final String message) {
        plugin.getLogger().warn(this.color(message));
    }

    @Override
    public void error(final String message) {
        plugin.getLogger().error(this.color(message));
    }

    @Override
    public void error(final String message, final Throwable t) {
        plugin.getLogger().error(this.color(message), t);
    }

    private String color(String message) {
        return LegacyComponentSerializer.legacySection().serialize(LegacyComponentSerializer
                .legacyAmpersand()
                .deserialize(message));
    }

    @Override
    public void runAsync(final Runnable task) {
        Sponge.asyncScheduler().submit(Task.builder().execute(task).build());
    }

}
