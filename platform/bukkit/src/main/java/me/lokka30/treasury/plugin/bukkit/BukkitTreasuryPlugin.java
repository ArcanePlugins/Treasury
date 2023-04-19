/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.lokka30.treasury.plugin.bukkit.vendor.BukkitVendor;
import me.lokka30.treasury.plugin.bukkit.vendor.CoreScheduler;
import me.lokka30.treasury.plugin.core.Platform;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import me.lokka30.treasury.plugin.core.config.settings.Settings;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import me.lokka30.treasury.plugin.core.utils.PluginVersion;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class BukkitTreasuryPlugin extends TreasuryPlugin implements Logger, ConfigAdapter {

    private final TreasuryBukkit plugin;
    private final PluginVersion pluginVersion;
    private final CoreScheduler coreScheduler;
    private Messages messages;
    private Settings settings;

    private final File messagesFile;
    private final File settingsFile;

    public BukkitTreasuryPlugin(@NotNull TreasuryBukkit plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.coreScheduler = new CoreScheduler(plugin);
        this.pluginVersion = new PluginVersion(plugin.getDescription().getVersion(), this);
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        settingsFile = new File(plugin.getDataFolder(), "settings.yml");
    }

    @Override
    public @NotNull PluginVersion getVersion() {
        return pluginVersion;
    }

    @Override
    public @NotNull Platform platform() {
        return BukkitVendor.getPlatformClass();
    }

    @Override
    public @NotNull Path pluginsFolder() {
        return plugin.getDataFolder().getParentFile().toPath();
    }

    @Override
    public @NotNull Logger logger() {
        return this;
    }

    @Override
    public @NotNull Scheduler scheduler() {
        return coreScheduler.getImpl();
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
    public boolean validatePluginJar(@NotNull File file) {
        return plugin.getPluginFile().getAbsoluteFile().getAbsolutePath().equalsIgnoreCase(file
                .getAbsoluteFile()
                .getAbsolutePath());
    }

    @Override
    public @NotNull Messages getMessages() {
        return messages;
    }

    @Override
    public @NotNull Settings getSettings() {
        return settings;
    }

    public String colorize(@NotNull String message) {
        message = colorizeHex(message);
        return BukkitVendor.isSpigot() ? net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&',
                message
        ) : ChatColor.translateAlternateColorCodes('&', message);
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-f0-9]{6})");

    private String colorizeHex(@NotNull String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(
                    buffer,
                    ChatColor.COLOR_CHAR + "x" + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(
                            1) + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(
                            3) + ChatColor.COLOR_CHAR + group.charAt(4) + ChatColor.COLOR_CHAR + group.charAt(
                            5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    @Override
    public void info(String message) {
        if (BukkitVendor.isPaper()) {
            plugin.getServer().getConsoleSender().sendMessage("[Treasury] " + colorize(message));
        } else {
            plugin.getLogger().info(colorize(message));
        }
    }

    @Override
    public void warn(String message) {
        if (BukkitVendor.isPaper()) {
            plugin
                    .getServer()
                    .getConsoleSender()
                    .sendMessage(colorize("&e[WARNING] [Treasury] " + message));
        } else {
            plugin.getLogger().warning(colorize(message));
        }
    }

    @Override
    public void error(String message) {
        if (BukkitVendor.isPaper()) {
            plugin
                    .getServer()
                    .getConsoleSender()
                    .sendMessage(colorize("&c[ERROR] [Treasury] " + message));
        } else {
            plugin.getLogger().severe(colorize(message));
        }
    }

    @Override
    public void error(String message, Throwable t) {
        // TODO: colorize() will NOT work on Paper
        plugin.getLogger().log(Level.SEVERE, colorize(message), t);
    }

}
