package me.lokka30.treasury.plugin.bungee;

import java.nio.file.Path;
import java.util.List;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.core.Platform;
import me.lokka30.treasury.plugin.core.ProviderEconomy;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import me.lokka30.treasury.plugin.core.config.settings.Settings;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import me.lokka30.treasury.plugin.core.utils.PluginVersion;
import org.jetbrains.annotations.NotNull;

public class BungeeTreasuryPlugin extends TreasuryPlugin implements Logger, Scheduler,
        ConfigAdapter {

    @Override
    public @NotNull PluginVersion getVersion() {
        return null;
    }

    @Override
    public @NotNull Platform platform() {
        return null;
    }

    @Override
    public @NotNull Path pluginsFolder() {
        return null;
    }

    @Override
    public @NotNull List<ProviderEconomy> allProviders() {
        return null;
    }

    @Override
    public void registerProvider(@NotNull final EconomyProvider newProvider) {

    }

    @Override
    public void reregisterProvider(
            @NotNull final ProviderEconomy provider,
            final boolean lowPriority
    ) {

    }

    @Override
    public void unregisterProvider(@NotNull final EconomyProvider provider) {

    }

    @Override
    public @NotNull Logger logger() {
        return null;
    }

    @Override
    public @NotNull Scheduler scheduler() {
        return null;
    }

    @Override
    public @NotNull ConfigAdapter configAdapter() {
        return null;
    }

    @Override
    public void reload() {

    }

    @Override
    public @NotNull List<String> pluginsListRegisteringProvider() {
        return null;
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

    }

    @Override
    public void warn(final String message) {

    }

    @Override
    public void error(final String message) {

    }

    @Override
    public void error(final String message, final Throwable t) {

    }

    @Override
    public void runSync(final Runnable task) {

    }

    @Override
    public void runAsync(final Runnable task) {

    }

}
