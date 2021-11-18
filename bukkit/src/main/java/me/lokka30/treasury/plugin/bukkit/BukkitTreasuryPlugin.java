package me.lokka30.treasury.plugin.bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.plugin.bukkit.fork.BukkitFork;
import me.lokka30.treasury.plugin.core.ProviderEconomy;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.config.messaging.ColorHandler;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import me.lokka30.treasury.plugin.core.config.settings.SettingKey;
import me.lokka30.treasury.plugin.core.config.settings.Settings;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// yeye I know single responsibility principle
// stop spanking me for doing this, but the heavy stuff have been abstracted away so there's literally no logic
// for the platform implementation to be split in different classes. SO STFU
public class BukkitTreasuryPlugin extends TreasuryPlugin
        implements Logger, Scheduler, ConfigAdapter, ColorHandler, Settings {

    private final Treasury plugin;
    private final BukkitFork fork;
    private MessagesImpl messages;

    private YamlConfiguration settings;

    private List<String> cachedPluginList = null;

    public BukkitTreasuryPlugin(@NotNull Treasury plugin) {
        this.fork = BukkitFork.get();
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.messages = new MessagesImpl(plugin);
    }

    public BukkitFork getFork() {
        return fork;
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
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
    public @NotNull ColorHandler colorHandler() {
        return this;
    }

    @Override
    public void reload() {
        messages = new MessagesImpl(plugin);
        messages.load();
        this.loadSettings(false);
    }

    public void loadSettings(boolean regenerate) {
        File settingsFile = new File(plugin.getDataFolder(), "settings.yml");
        if (regenerate && settingsFile.exists()) {
            // todo: this is not the correct way of dumping new options. find another way!!!!
            settingsFile.delete();
        }
        if (!settingsFile.exists()) {
            if (!settingsFile.getParentFile().exists()) {
                settingsFile.getParentFile().mkdirs();
            }
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("settings.yml")) {
                Files.copy(in, settingsFile.getAbsoluteFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        settings = YamlConfiguration.loadConfiguration(settingsFile);
    }

    @Override
    public @NotNull EconomyAPIVersion getEconomyAPIVersion() {
        return Treasury.ECONOMY_API_VERSION;
    }

    @Override
    public @NotNull List<String> pluginsList() {
        if (cachedPluginList != null) {
            return cachedPluginList;
        }
        cachedPluginList = Arrays.stream(
                Bukkit.getPluginManager().getPlugins()
        ).map(Plugin::getName).collect(Collectors.toList());
        return cachedPluginList;
    }

    @Override
    public @NotNull Messages getMessages() {
        return messages;
    }

    @Override
    public @NotNull Settings getSettings() {
        return this;
    }

    @Override
    public @NotNull String colorize(@NotNull String message) {
        return fork.isSpigot()
                ? net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message)
                : org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public <T> @NotNull T getSetting(@NotNull SettingKey<T> settingKey) {
        Objects.requireNonNull(settingKey, "settingKey");
        if (settingKey.getSpecialMapper() != null) {
            return settingKey.getSpecialMapper().apply(this);
        }
        Object value = getSetting(settingKey.getKey());
        if (value == null) {
            this.loadSettings(true);
            return settingKey.getDefault();
        }
        Class<T> type = settingKey.getType();
        if (type.isAssignableFrom(value.getClass())) {
            return settingKey.getType().cast(value);
        }
        if (type.isEnum()) {
            String valString = String.valueOf(value).toUpperCase(Locale.ROOT);
            for (T eConst : type.getEnumConstants()) {
                String name = eConst.toString();
                if (valString.equalsIgnoreCase(name)) {
                    return eConst;
                }
            }
            error("Invalid enum constant " + value + " for config option " + settingKey.getKey());
        }
        return settingKey.getDefault();
    }

    @Override
    public @Nullable Object getSetting(@NotNull String key) {
        return settings.get(key);
    }

    @Override
    public @Nullable List<String> getStringList(@NotNull String key) {
        return settings.getStringList(key);
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
