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

package me.lokka30.treasury.plugin.core;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import me.lokka30.treasury.plugin.core.utils.PluginVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Treasury core implementations must implement this class and set its instance in order for the core
 * to function properly.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public abstract class TreasuryPlugin {

    private static TreasuryPlugin instance;

    /**
     * Returns the instance set for this class.
     *
     * @return instance
     */
    public static TreasuryPlugin getInstance() {
        return instance;
    }

    /**
     * Sets an instance for this treasury plugin.
     *
     * @param newInstance the instance set
     * @throws IllegalArgumentException if instance is already set
     */
    public static void setInstance(@NotNull TreasuryPlugin newInstance) {
        Objects.requireNonNull(newInstance, "newInstance");
        if (instance != null) {
            throw new IllegalArgumentException("Instance already set");
        }
        instance = newInstance;
    }

    /**
     * Returns the version of the treasury plugin.
     *
     * @return version
     */
    @NotNull
    public abstract PluginVersion getVersion();

    /**
     * Returns the description of the treasury plugin.
     *
     * @return description
     */
    @Nullable
    public abstract String getDescription();

    /**
     * Returns the first {@link ProviderEconomy}
     *
     * @return highest priority provider of economy provider
     */
    @Nullable
    public ProviderEconomy economyProviderProvider() {
        List<ProviderEconomy> allProviders = allProviders();
        if (allProviders.isEmpty()) {
            return null;
        }
        return allProviders.get(0);
    }

    /**
     * Should give all the economy providers registered, ordered by highest priority.
     *
     * @return ordered providers list
     */
    @NotNull
    public abstract List<ProviderEconomy> allProviders();

    /**
     * Should register the specified {@link EconomyProvider} at the highest priority.
     *
     * @param newProvider provider to register
     */
    public abstract void registerProvider(@NotNull EconomyProvider newProvider);

    /**
     * Should re register the specified {@link ProviderEconomy} at the priority defined by the {@code lowPriority}
     * param.
     *
     * @param provider    the provider to re register
     * @param lowPriority should register on low priority
     */
    public abstract void reregisterProvider(@NotNull ProviderEconomy provider, boolean lowPriority);

    /**
     * Should unregister the specified {@link EconomyProvider}
     *
     * @param provider provider to unregister
     */
    public abstract void unregisterProvider(@NotNull EconomyProvider provider);

    /**
     * Returns the logger wrapper.
     *
     * @return logger
     */
    @NotNull
    public abstract Logger logger();

    /**
     * Returns the scheduler wrapper.
     *
     * @return scheduler
     */
    @NotNull
    public abstract Scheduler scheduler();

    /**
     * Returns the config adapter.
     *
     * @return config adapter
     */
    @NotNull
    public abstract ConfigAdapter configAdapter();

    /**
     * Should reload the plugin
     */
    public abstract void reload();

    /**
     * Returns the plugins' names, which are registering an economy provider,
     * as a list with {@link String strings}, as this is being used in
     * {@link me.lokka30.treasury.plugin.core.command.subcommand.migrate.MigrateSubcommand}.
     *
     * @return plugins' names
     */
    @NotNull
    public abstract List<String> pluginsListRegisteringProvider();
}
