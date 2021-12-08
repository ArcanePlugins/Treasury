/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.bukkit;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.plugin.bukkit.vendor.BukkitVendor;
import me.lokka30.treasury.plugin.core.ProviderEconomy;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import me.lokka30.treasury.plugin.core.config.settings.Settings;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import me.lokka30.treasury.plugin.core.utils.PluginVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

// yeye I know single responsibility principle
// stop spanking me for doing this, but the heavy stuff have been abstracted away so there's literally no logic
// for the platform implementation to be split in different classes. SO STFU
public class BukkitTreasuryPlugin extends TreasuryPlugin
        implements Logger, Scheduler, ConfigAdapter {

    private final TreasuryBukkit plugin;
    private final PluginVersion pluginVersion;
    private Messages messages;
    private Settings settings;

    private final File messagesFile;
    private final File settingsFile;

    private List<String> cachedPluginList = null;

    public BukkitTreasuryPlugin(@NotNull TreasuryBukkit plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.pluginVersion = new PluginVersion(plugin.getDescription().getVersion(), this);
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        settingsFile = new File(plugin.getDataFolder(), "settings.yml");
    }

    @Override
    public @NotNull PluginVersion getVersion() {
        return pluginVersion;
    }

    @Override
    public @Nullable String getDescription() {
        return plugin.getDescription().getDescription();
    }

    @Override
    public @NotNull List<ProviderEconomy> allProviders() {
        Collection<RegisteredServiceProvider<EconomyProvider>> providers =
                Bukkit.getServicesManager().getRegistrations(EconomyProvider.class);
        List<ProviderEconomy> ret = new ArrayList<>();
        for (RegisteredServiceProvider<EconomyProvider> rsp : providers) {
            ret.add(new ProviderEconomyImpl(rsp.getPriority(), new RegistrarInfoImpl(rsp.getPlugin()), rsp.getProvider()));
        }
        if (!ret.isEmpty()) {
            ret.sort(((Comparator<ProviderEconomy>) (o1, o2) -> {
                ServicePriority priority1 = ServicePriority.valueOf(capitalizeFirstLetter(o1.getPriority()));
                ServicePriority priority2 = ServicePriority.valueOf(capitalizeFirstLetter(o2.getPriority()));
                return priority1.compareTo(priority2);
            }).reversed());
        }
        return ret;
    }

    private String capitalizeFirstLetter(String val) {
        char[] c = val.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

    @Override
    public void registerProvider(@NotNull EconomyProvider newProvider) {
        Bukkit.getServicesManager().register(EconomyProvider.class, newProvider, plugin, ServicePriority.Highest);
    }

    @Override
    public void reregisterProvider(@NotNull ProviderEconomy provider, boolean lowPriority) {
        ServicePriority priority = lowPriority ? ServicePriority.Low : ServicePriority.High;
        if (provider.getPriority().equalsIgnoreCase(priority.name().toLowerCase(Locale.ROOT))) {
            return;
        }
        Plugin plugin = Bukkit.getPluginManager().getPlugin(provider.registrar().getName());
        EconomyProvider eco = provider.provide();
        Bukkit.getServicesManager().unregister(eco);
        Bukkit.getServicesManager().register(EconomyProvider.class, eco, plugin, priority);
    }

    @Override
    public void unregisterProvider(@NotNull EconomyProvider provider) {
        runSync(() -> Bukkit.getServicesManager().unregister(provider));
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
    public @NotNull List<String> pluginsListRegisteringProvider() {
        if (cachedPluginList != null) {
            return cachedPluginList;
        }
        cachedPluginList = Arrays.stream(
                Bukkit.getPluginManager().getPlugins()
        ).filter(pl -> {
            List<RegisteredServiceProvider<?>> registrations = Bukkit.getServicesManager().getRegistrations(pl);
            if (registrations.isEmpty()) {
                return false;
            }
            if (registrations.size() == 1) {
                return registrations.get(0).getProvider().getClass().isAssignableFrom(EconomyProvider.class);
            }
            boolean hasRegistration = false;
            for (RegisteredServiceProvider<?> provider : registrations) {
                if (provider.getProvider().getClass().isAssignableFrom(EconomyProvider.class)) {
                    hasRegistration = true;
                    break;
                }
            }
            return hasRegistration;
        }).map(Plugin::getName).collect(Collectors.toList());
        return cachedPluginList;
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
        return BukkitVendor.isSpigot()
                ? net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message)
                : org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public void info(String message) {
        plugin.getLogger().info(colorize(message));
    }

    @Override
    public void warn(String message) {
        plugin.getLogger().warning(colorize(message));
    }

    @Override
    public void error(String message) {
        plugin.getLogger().severe(colorize(message));
    }

    @Override
    public void error(String message, Throwable t) {
        plugin.getLogger().log(Level.SEVERE, colorize(message), t);
    }

    @Override
    public void runSync(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }

    @Override
    public void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }
}
