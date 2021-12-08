/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.utils;

import com.google.gson.JsonObject;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.plugin.core.ProviderEconomy;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

// TODO: This requires a Spigot Resource ID which can't be obtained before the resource is released.
/**
 * Represents an update checker for spigot.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public final class UpdateChecker {

    private static final int RESOURCE_ID = 12345;

    private static final String REQUEST_URI = String.format(
            "https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=%s", RESOURCE_ID
    );

    /**
     * Runs an update check.
     */
    public static void checkForUpdates() {
        //noinspection ConstantConditions
        if (true) return;
        TreasuryPlugin plugin = TreasuryPlugin.getInstance();
        if (!plugin.configAdapter().getSettings().checkForUpdates()) {
            return;
        }
        plugin.scheduler().runAsync(() -> {
            try {
                URL url = new URL(REQUEST_URI);
                try (Reader in = new InputStreamReader(url.openStream())) {
                    JsonObject object = Utils.GSON.fromJson(in, JsonObject.class);
                    String latestVersion = object.get("current_version").getAsString();
                    handleVersionCheck(latestVersion);
                }
            } catch (IOException e) {
                plugin.logger().error("IO whilst trying to request Spigot", e);
            }
        });
    }

    private static void handleVersionCheck(String readString) {
        TreasuryPlugin plugin = TreasuryPlugin.getInstance();
        PluginVersion latestVersionRead = new PluginVersion(readString, plugin.logger());
        PluginVersion currentVersion = plugin.getVersion();
        PluginVersion.ComparisonResult comparisonResult = currentVersion.compare(latestVersionRead);

        ProviderEconomy providerEconomyProvider = plugin.economyProviderProvider();
        if (providerEconomyProvider == null) {
            handlePluginVersioning(plugin, comparisonResult, latestVersionRead);
        } else {
            EconomyProvider provider = providerEconomyProvider.provide();
            EconomyAPIVersion providerVersion = provider.getSupportedAPIVersion();
            EconomyAPIVersion latestVersion = EconomyAPIVersion.getCurrentAPIVersion();

            handlePluginVersioning(plugin, comparisonResult, latestVersionRead);

            PluginVersion.ComparisonResult apiComparisonResult = Utils.compareAPIVersions(providerVersion, latestVersion);
            if (apiComparisonResult == PluginVersion.ComparisonResult.OLDER) {
                // this means that "providerVersion" is older than the latest version
                plugin.logger().error(
                        "Economy provider going by the plugin name '" + providerEconomyProvider.registrar().getName() + "'" +
                                "is utilising an older version of the Treasury Economy API"
                );
                plugin.logger().error("than what your current version of Treasury provides. You should inform the author(s)");
                plugin.logger().error("of that plugin that they should update their resource to use the newer Treasury Economy API");
                plugin.logger().error(" ");
                if (comparisonResult == PluginVersion.ComparisonResult.NEWER) {
                    plugin.logger().warn("Before updating Treasury, ensure that your Economy Provider utilizes");
                    plugin.logger().warn("the latest version of the Treasury Economy API.");
                    plugin.logger().warn(" ");
                }
                plugin.logger().error("You must resolve this issue as soon as possible. Leaving this issue unresolved can");
                plugin.logger().error("cause errors with your Economy Provider, and therefore, have the potential to severely harm your server's economy");
            } else if (apiComparisonResult == PluginVersion.ComparisonResult.NEWER) {
                // this means that "providerVersion" is newer than the latest version
                plugin.logger().error(
                        "Economy provider going by the plugin name '" + providerEconomyProvider.registrar().getName() + "'" +
                                "is utilising a newer version of the Treasury Economy API"
                );
                plugin.logger().error("than what your current version of Treasury provides.");
                plugin.logger().error(" ");
                if (comparisonResult == PluginVersion.ComparisonResult.EQUAL) {
                    if (!currentVersion.isDevelopmentVersion()) {
                        plugin.logger().warn("Since you seem to be running the latest version of Treasury, please check if your");
                        plugin.logger().warn("Economy Provider expects you to run a development build of Treasury instead of a release build.");
                    } else {
                        plugin.logger().warn("Check if there are any newer development builds available which have a newer Economy API version.");
                    }
                } else if (comparisonResult == PluginVersion.ComparisonResult.NEWER) {
                    plugin.logger().warn("As mentioned up, a Treasury plugin update is available for download - this may resolve");
                    plugin.logger().warn("the mismatching API versions.");
                } else if (comparisonResult == PluginVersion.ComparisonResult.OLDER) {
                    plugin.logger().warn("Check if there are any newer development builds available which have a newer Economy API version.");
                }
                plugin.logger().error(" ");
                plugin.logger().error("You must resolve this issue as soon as possible. Leaving this issue unresolved can");
                plugin.logger().error("cause errors with your Economy Provider, and therefore, have the potential to severely harm your server's economy");
            }
        }
    }

    private static void handlePluginVersioning(TreasuryPlugin plugin, PluginVersion.ComparisonResult comparisonResult, PluginVersion latestVersionRead) {
        PluginVersion currentVersion = plugin.getVersion();
        if (comparisonResult == PluginVersion.ComparisonResult.NEWER) {
            // this statement means that the "latestVersionRead" is newer than the version we're running.
            plugin.logger().warn(
                    "A new Treasury update is available - '&bv" + latestVersionRead
                            + "&e' - please update as soon as possible."
                            + " &8(&7You're running '&bv" + currentVersion + "&7'&8)"
            );
        } else if (comparisonResult == PluginVersion.ComparisonResult.EQUAL) {
            // this statement means that the "latestVersionRead" is equal to the version we're running
            if (currentVersion.isDevelopmentVersion()) {
                plugin.logger().warn("You are running a development version of Treasury. " +
                        "If there are any issues, please report them to: https://github.com/lokka30/Treasury/issues .");
                plugin.logger().error("USE WITH CAUTION");
                plugin.logger().warn("Be aware that there might be a new development release.");
            }
        } else if (comparisonResult == PluginVersion.ComparisonResult.OLDER) {
            // this statement means that the "latestVersionRead" is older than the version we're running.
            if (currentVersion.isDevelopmentVersion()) {
                plugin.logger().warn("You are running a development version of Treasury. " +
                        "If there are any issues, please report them to: https://github.com/lokka30/Treasury/issues .");
                plugin.logger().error("USE WITH CAUTION");
                plugin.logger().warn("Be aware that there might be a new development release.");
            } else {
                plugin.logger().warn("You are running a newer version of Treasury than known. How did we get here?");
            }
        }
    }
}
