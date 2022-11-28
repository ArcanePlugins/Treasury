/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.minestom;

import java.io.File;
import java.nio.file.Path;
import me.lokka30.treasury.plugin.core.Platform;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import me.lokka30.treasury.plugin.core.config.settings.Settings;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import me.lokka30.treasury.plugin.core.utils.PluginVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

public class MinestomTreasuryPlugin extends TreasuryPlugin implements Logger, Scheduler,
        ConfigAdapter {

    private final TreasuryMinestom plugin;
    private final PluginVersion version;

    private Messages messages;
    private Settings settings;

    private final File messagesFile, settingsFile;

    public MinestomTreasuryPlugin(TreasuryMinestom plugin) {
        this.plugin = plugin;
        messagesFile = new File(plugin.getDataDirectory().toFile(), "messages.yml");
        settingsFile = new File(plugin.getDataDirectory().toFile(), "settings.yml");

        this.version = new PluginVersion(plugin.getOrigin().getVersion(), this);
    }

    @Override
    public @NotNull PluginVersion getVersion() {
        return version;
    }

    @Override
    public @NotNull Platform platform() {
        return Platform.MINESTOM;
    }

    @Override
    public @NotNull Path pluginsFolder() {
        return MinecraftServer.getExtensionManager().getExtensionDataRoot();
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
        return messages;
    }

    @Override
    public @NotNull Settings getSettings() {
        return settings;
    }

    @Override
    public void info(final String message) {
        plugin.getLogger().info(this.deserialize(message));
    }

    @Override
    public void warn(final String message) {
        plugin.getLogger().warn(this.deserialize(message));
    }

    @Override
    public void error(final String message) {
        plugin.getLogger().error(this.deserialize(message));
    }

    @Override
    public void error(final String message, final Throwable t) {
        plugin.getLogger().error(this.deserialize(message), t);
    }

    public Component deserialize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    @Override
    public void runSync(final Runnable task) {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            task.run();
            return TaskSchedule.immediate();
        }, ExecutionType.SYNC);
    }

    @Override
    public void runAsync(final Runnable task) {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            task.run();
            return TaskSchedule.immediate();
        }, ExecutionType.ASYNC);
    }

}
