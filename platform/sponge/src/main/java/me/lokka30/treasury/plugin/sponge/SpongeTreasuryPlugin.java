/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

public class SpongeTreasuryPlugin extends TreasuryPlugin implements Logger, Scheduler,
        ConfigAdapter {

    private final TreasurySponge plugin;
    private final PluginVersion version;
    private final Platform platform;

    private Messages messages;
    private SpongeSettings settings;

    private final File messagesFile, settingsFile;
    private final Path pluginsFolder;

    public SpongeTreasuryPlugin(TreasurySponge plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        messagesFile = new File(plugin.getDataDir().toFile(), "messages.yml");
        settingsFile = new File(plugin.getDataDir().toFile(), "settings.yml");

        pluginsFolder = plugin.getDataDir().getParent().getParent().resolve("mods");

        this.version = new PluginVersion(
                plugin.getContainer().metadata().version().toString(),
                this
        );
        this.platform = new Platform("Sponge", "");
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
        return pluginsFolder;
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
        settings = SpongeSettings.loadSponge(settingsFile);
    }

    @Override
    public @NotNull Messages getMessages() {
        return this.messages;
    }

    @Override
    public @NotNull SpongeSettings getSettings() {
        return this.settings;
    }

    @Override
    public void info(final String message) {
        plugin.getLogger().info(this.stripColor(message));
    }

    @Override
    public void warn(final String message) {
        plugin.getLogger().warn(this.stripColor(message));
    }

    @Override
    public void error(final String message) {
        plugin.getLogger().error(this.stripColor(message));
    }

    @Override
    public void error(final String message, final Throwable t) {
        plugin.getLogger().error(this.stripColor(message), t);
    }

    public Component color(String message) {
        if (message.indexOf('&') == -1) {
            return Component.text(message);
        }
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    private String stripColor(String message) {
        if (message.indexOf('&') == -1) {
            return message;
        }
        return PlainTextComponentSerializer.plainText().serialize(this.color(message));
    }

    @Override
    public void runSync(final Runnable task) {
        Sponge.server().scheduler().submit(Task
                .builder()
                .plugin(this.plugin.getContainer())
                .execute(task)
                .build());
    }

    @Override
    public void runAsync(final Runnable task) {
        Sponge.asyncScheduler().submit(Task
                .builder()
                .plugin(this.plugin.getContainer())
                .execute(task)
                .build());
    }

}
