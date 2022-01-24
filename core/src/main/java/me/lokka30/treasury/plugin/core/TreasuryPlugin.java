/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate.EconomyMigrateSub;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import me.lokka30.treasury.plugin.core.utils.PluginVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Treasury core implementations must implement this class and set its instance in order for the core
 * to function properly.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public abstract class TreasuryPlugin {

    /**
     * Description of the plugin.
     */
    public static final String DESCRIPTION = "Treasury is a modern multi-platform library facilitating " + "the integration between service providing/consuming " + "plugins on Minecraft servers. ";

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
     * Returns the platform on which the treasury plugin is running.
     *
     * @return platform
     */
    @NotNull
    public abstract Platform platform();

    /**
     * Returns the path to the platform we're running onto - plugin directory.
     *
     * @return plugins folder
     */
    @NotNull
    public abstract Path pluginsFolder();

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
     * {@link EconomyMigrateSub}.
     *
     * @return plugins' names
     */
    @NotNull
    public abstract List<String> pluginsListRegisteringProvider();

    /**
     * A method, called on latest Treasury plugin download, which may or may not validate the
     * current Treasury jar running, whether it is that or not.
     *
     * @param file the file to check
     * @return whether valid or not if implemented by a platform, if not implemented - it assumes
     *         the file is the correct one
     */
    public boolean validatePluginJar(@NotNull File file) {
        return true;
    }

}
