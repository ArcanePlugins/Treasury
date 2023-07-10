/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core;

import com.mrivanplays.process.ProcessScheduler;
import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate.EconomyMigrateSub;
import me.lokka30.treasury.plugin.core.config.ConfigAdapter;
import me.lokka30.treasury.plugin.core.logging.Logger;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import me.lokka30.treasury.plugin.core.utils.PluginVersion;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import org.jetbrains.annotations.NotNull;

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
    public static final String DESCRIPTION =
            "Treasury is a modern multi-platform library facilitating " +
                    "the integration between service providing/consuming " +
                    "plugins on Minecraft servers. ";

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

    private List<String> economyProviderRegistrars = null;

    /**
     * Returns the plugins' names, which are registering an economy provider,
     * as a list with {@link String strings}, as this is being used in
     * {@link EconomyMigrateSub}.
     *
     * @return plugins' names
     */
    @NotNull
    public List<String> pluginsListRegisteringEconomyProvider() {
        if (economyProviderRegistrars != null) {
            return economyProviderRegistrars;
        }
        Set<Service<EconomyProvider>> services =
                ServiceRegistry.INSTANCE.allServicesFor(EconomyProvider.class);
        if (services.isEmpty()) {
            economyProviderRegistrars = Collections.emptyList();
        } else {
            economyProviderRegistrars = services.stream()
                    .map(Service::registrarName)
                    .collect(Collectors.toList());
        }
        return economyProviderRegistrars;
    }

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

    private ProcessScheduler processScheduler;

    /**
     * Returns the {@link ProcessScheduler} instance, required for scheduling
     * {@link com.mrivanplays.process.Process processes}.
     *
     * @return process scheduler
     */
    public ProcessScheduler processScheduler() {
        if (this.processScheduler == null) {
            this.processScheduler = new ProcessScheduler((task) -> scheduler().runAsync(task));
        }
        return processScheduler;
    }

    /**
     * Logs the plugin startup message
     *
     * @param startupTimer startup timer
     */
    public void logStartupMessage(QuickTimer startupTimer) {
            this.logger().info("Running on " + this.platform().displayName());
            this.logger().info("Start-up complete (took " + startupTimer.getTimer() + "ms).");
    }

    /**
     * Due to the shut-down logic being the same on all the platforms, the disable method has
     * been abstracted into here.
     *
     * @param psShutdownCode platform specific shutdown code to run
     */
    public void shutdown(Runnable psShutdownCode) {
        QuickTimer shutdownTimer = new QuickTimer();

        if (psShutdownCode != null) {
            psShutdownCode.run();
        }

        this.logger().info("Shut-down complete (took " + shutdownTimer.getTimer() + "ms).");
    }

}
