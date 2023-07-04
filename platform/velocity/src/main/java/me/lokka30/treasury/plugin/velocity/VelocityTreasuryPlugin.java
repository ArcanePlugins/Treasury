/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.velocity;

import com.velocitypowered.api.plugin.PluginContainer;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
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

public class VelocityTreasuryPlugin extends TreasuryPlugin implements Logger, Scheduler,
        ConfigAdapter {

    private final TreasuryVelocity plugin;
    private final PluginVersion version;
    private final Platform platform;
    private Messages messages;
    private Settings settings;

    private final File messagesFile;
    private final File settingsFile;

    public VelocityTreasuryPlugin(@NotNull TreasuryVelocity plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        Optional<PluginContainer> containerOpt = plugin.getProxy().getPluginManager().fromInstance(
                plugin);
        if (!containerOpt.isPresent()) {
            throw new IllegalArgumentException(
                    "Something went wrong ; containerOpt.isPresent = false. Please report to developers of Treasury.");
        }
        this.version = new PluginVersion(
                containerOpt.get().getDescription().getVersion().get(),
                this
        );
        messagesFile = plugin.getDataDirectory().resolve("messages.yml").toFile();
        settingsFile = plugin.getDataDirectory().resolve("settings.yml").toFile();
        this.platform = new Platform("Velocity", "");
    }


    @Override
    public @NotNull PluginVersion getVersion() {
        return version;
    }

    @Override
    public @NotNull Platform platform() {
        return this.platform;
    }

    @Override
    public @NotNull Path pluginsFolder() {
        return plugin.getDataDirectory().getParent();
    }

    public Path getDataDirectory() {
        return plugin.getDataDirectory();
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
        loadMessages();
        loadSettings();
    }

    public void loadMessages() {
        messages = Messages.load(messagesFile);
    }

    public void loadSettings() {
        settings = Settings.load(settingsFile);
    }

    @Override
    public @NotNull Messages getMessages() {
        return messages;
    }

    @Override
    public @NotNull Settings getSettings() {
        return settings;
    }

    // adventure parses hex colors for us :)
    private String color(String message) {
        return LegacyComponentSerializer.legacySection().serialize(LegacyComponentSerializer
                .legacyAmpersand()
                .deserialize(message));
    }

    @Override
    public void info(final String message) {
        plugin.getLogger().info(color(message));
    }

    @Override
    public void warn(final String message) {
        plugin.getLogger().warn(color(message));
    }

    @Override
    public void error(final String message) {
        plugin.getLogger().error(color(message));
    }

    @Override
    public void error(final String message, final Throwable t) {
        plugin.getLogger().error(color(message), t);
    }

    @Override
    public void runSync(final Runnable task) {
        task.run();
    }

    @Override
    public int runAsync(final Runnable task) {
        plugin.getProxy().getScheduler().buildTask(plugin, task).schedule();
        return -1; // velocity doesn't have such task id system, and we also don't need it
    }

    @Override
    public void cancelTask(final int id) {
        // don't need this here
    }

}
