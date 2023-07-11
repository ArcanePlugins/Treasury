package me.lokka30.treasury.plugin.krypton;

import java.nio.file.Path;
import me.lokka30.treasury.plugin.core.Platform;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import me.lokka30.treasury.plugin.core.config.settings.Settings;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import me.lokka30.treasury.plugin.core.utils.PluginVersion;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.api.scheduling.ExecutionType;
import org.kryptonmc.api.scheduling.TaskTime;

public class KryptonTreasuryPlugin extends TreasuryPlugin implements Logger, Scheduler,
        ConfigAdapter {

    private final TreasuryKrypton plugin;
    private final Platform platform;
    private final PluginVersion version;

    public KryptonTreasuryPlugin(TreasuryKrypton plugin) {
        this.plugin = plugin;
        this.platform = new Platform("Krypton", "");
        this.version = new PluginVersion(
                plugin.server().getPluginManager().fromInstance(plugin).getDescription().version(),
                this
        );
    }

    @Override
    public @NotNull PluginVersion getVersion() {
        return this.version;
    }

    @Override
    public @NotNull Platform platform() {
        return this.platform;
    }

    @Override
    public @NotNull Path pluginsFolder() {
        return null;
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

    }

    @Override
    public @NotNull Messages getMessages() {
        return null;
    }

    @Override
    public @NotNull Settings getSettings() {
        return null;
    }

    @Override
    public void info(final String message) {
        plugin.logger().info(message);
    }

    @Override
    public void warn(final String message) {
        plugin.logger().warn(message);
    }

    @Override
    public void error(final String message) {
        plugin.logger().error(message);
    }

    @Override
    public void error(final String message, final Throwable t) {
        plugin.logger().error(message, t);
    }

    @Override
    public void runSync(final Runnable task) {
        plugin.server().getScheduler().scheduleTask(task,
                TaskTime.zero(),
                TaskTime.zero(),
                ExecutionType.SYNCHRONOUS
        );
    }

    @Override
    public void runAsync(final Runnable task) {
        plugin.server().getScheduler().scheduleTask(task,
                TaskTime.zero(),
                TaskTime.zero(),
                ExecutionType.ASYNCHRONOUS
        );
    }

}
