/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.time.OffsetDateTime;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.plugin.core.ProviderEconomy;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;

// TODO: This requires a Spigot Resource ID which can't be obtained before the resource is released.

/**
 * Represents an update checker for spigot.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public final class UpdateChecker {

    private static final int RESOURCE_ID = 12345;

    private static final String SPIGOT_REQUEST_URI = String.format("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=%s",
            RESOURCE_ID
    );

    private static final String GITHUB_COMMITS_URI = "https://api.github.com/repos/lokka30/Treasury/commits";

    /**
     * Runs an update check.
     */
    public static void checkForUpdates() {
        //noinspection ConstantConditions
        if (true) {
            return;
        }
        TreasuryPlugin plugin = TreasuryPlugin.getInstance();
        if (!plugin.configAdapter().getSettings().checkForUpdates()) {
            return;
        }
        // since spigot is going to get only releases, we shall check whether this version is a
        // release first and if it is a release, check spigot, otherwise check github commits
        PluginVersion current = plugin.getVersion();
        if (current.isReleaseVersion()) {
            plugin.scheduler().runAsync(() -> {
                try {
                    URL url = new URL(SPIGOT_REQUEST_URI);
                    try (Reader in = new InputStreamReader(url.openStream())) {
                        JsonObject object = Utils.GSON.fromJson(in, JsonObject.class);
                        String latestVersion = object.get("current_version").getAsString();
                        handleSpigotVersionCheck(latestVersion);
                    }
                } catch (IOException e) {
                    plugin.logger().error(
                            "Unable to retrieve the latest version data for the Treasury resource on SpigotMC.org (IOException)",
                            e
                    );
                }
            });
        } else if (current.isDevelopmentVersion()) {
            plugin.scheduler().runAsync(() -> {
                try {
                    URL commits = new URL(GITHUB_COMMITS_URI);
                    try (Reader commitsIn = new InputStreamReader(commits.openStream())) {
                        JsonObject first = Utils.GSON
                                .fromJson(commitsIn, JsonArray.class)
                                .get(0)
                                .getAsJsonObject();
                        // we can't really compare SHA's as we have an abbreviated SHA rather
                        // than the full commit SHA, that's why we're comparing dates
                        OffsetDateTime latestTime = OffsetDateTime.parse(first
                                .get("commit")
                                .getAsJsonObject()
                                .get("author")
                                .getAsJsonObject()
                                .get("date")
                                .getAsString());

                        // now get our commit date
                        OffsetDateTime madeIn = OffsetDateTime.parse(UpdateChecker.class
                                .getPackage()
                                .getImplementationVersion());

                        PluginVersion.ComparisonResult comparisonResult;
                        if (madeIn.isBefore(latestTime)) {
                            // running outdated
                            comparisonResult = PluginVersion.ComparisonResult.NEWER;
                        } else if (madeIn.isAfter(latestTime)) {
                            // running newer
                            comparisonResult = PluginVersion.ComparisonResult.OLDER;
                        } else if (madeIn.isEqual(latestTime)) {
                            // equal
                            comparisonResult = PluginVersion.ComparisonResult.EQUAL;
                        } else {
                            // how did we get here?
                            comparisonResult = PluginVersion.ComparisonResult.UNKNOWN;
                        }

                        handleGitHubVersioning(comparisonResult);
                    }
                } catch (IOException e) {
                    plugin.logger().error(
                            "Unable to retrieve the latest version data for the Treasury resource on GitHub.com (IOException)",
                            e
                    );
                }
            });
        } else {
            // how did we get here?
            plugin.logger().warn(
                    "Illegal version of Treasury detected. Something didn't stop the plugin from running already, so you're seeing this. If you are running a development build or you're a developer running live environment, please change this ASAP!");
        }
    }

    private static void handleSpigotVersionCheck(String readString) {
        TreasuryPlugin plugin = TreasuryPlugin.getInstance();
        PluginVersion latestVersionRead = new PluginVersion(readString, plugin.logger());
        PluginVersion currentVersion = plugin.getVersion();
        PluginVersion.ComparisonResult comparisonResult = currentVersion.compare(latestVersionRead);

        if (comparisonResult == PluginVersion.ComparisonResult.NEWER) {
            // this statement means that the "latestVersionRead" is newer than the version we're running.
            plugin
                    .logger()
                    .warn("A new Treasury update is available - '&bv" + latestVersionRead + "&e' - please update as soon as possible. &8(&7You're running '&bv" + currentVersion + "&7'&8)");
        } else if (comparisonResult == PluginVersion.ComparisonResult.OLDER) {
            // this statement means that the "latestVersionRead" is older than the version we're running.
            plugin.logger().warn(
                    "You are running a newer version of Treasury than known. How did we get here?");
        }

        ProviderEconomy providerEconomyProvider = plugin.economyProviderProvider();
        if (providerEconomyProvider != null) {
            EconomyProvider provider = providerEconomyProvider.provide();
            EconomyAPIVersion providerVersion = provider.getSupportedAPIVersion();
            EconomyAPIVersion latestVersion = EconomyAPIVersion.getCurrentAPIVersion();

            handleAPIVersioning(providerVersion,
                    latestVersion,
                    providerEconomyProvider,
                    comparisonResult,
                    currentVersion.isDevelopmentVersion()
            );
        }
    }

    private static void handleGitHubVersioning(PluginVersion.ComparisonResult comparisonResult) {
        TreasuryPlugin plugin = TreasuryPlugin.getInstance();
        if (comparisonResult == PluginVersion.ComparisonResult.NEWER) {
            // this statement means that the "latestVersionRead" is newer than the version we're running.
            plugin.logger().warn(
                    "You are running an outdated development version. There may be issues that have been fixed in the newer development versions. Please update as soon as possible.");
        } else if (comparisonResult == PluginVersion.ComparisonResult.OLDER) {
            // this statement means that the "latestVersionRead" is older than the version we're running.
            plugin
                    .logger()
                    .warn("You are running a newer development version. How did we get here?");
        } else if (comparisonResult == PluginVersion.ComparisonResult.EQUAL) {
            plugin.logger().warn(
                    "You are running a development version of Treasury. If there are any issues, please report them to: https://github.com/lokka30/Treasury/issues .");
            plugin.logger().error("USE WITH CAUTION");
        } else if (comparisonResult == PluginVersion.ComparisonResult.UNKNOWN) {
            plugin.logger().warn(
                    "Couldn't check for a development version update. You are running a development version. Please check for updates and stay updated :).");
        }

        ProviderEconomy providerEconomyProvider = plugin.economyProviderProvider();
        if (providerEconomyProvider != null) {
            EconomyProvider provider = providerEconomyProvider.provide();
            EconomyAPIVersion providerVersion = provider.getSupportedAPIVersion();
            EconomyAPIVersion latestVersion = EconomyAPIVersion.getCurrentAPIVersion();

            handleAPIVersioning(providerVersion,
                    latestVersion,
                    providerEconomyProvider,
                    comparisonResult,
                    plugin.getVersion().isDevelopmentVersion()
            );
        }
    }

    private static void handleAPIVersioning(
            EconomyAPIVersion providerVersion,
            EconomyAPIVersion latestVersion,
            ProviderEconomy providerEconomyProvider,
            PluginVersion.ComparisonResult comparisonResult,
            boolean currentVersionDevelopment
    ) {
        TreasuryPlugin plugin = TreasuryPlugin.getInstance();
        PluginVersion.ComparisonResult apiComparisonResult = Utils.compareAPIVersions(providerVersion,
                latestVersion
        );
        if (apiComparisonResult == PluginVersion.ComparisonResult.OLDER) {
            // this means that "providerVersion" is older than the latest version
            plugin
                    .logger()
                    .error("Economy provider going by the plugin name '" + providerEconomyProvider
                            .registrar()
                            .getName() + "'" + "is utilising an older version of the Treasury Economy API");
            plugin.logger().error(
                    "than what your current version of Treasury provides. You should inform the author(s)");
            plugin.logger().error(
                    "of that plugin that they should update their resource to use the newer Treasury Economy API");
            plugin.logger().error(" ");
            if (comparisonResult == PluginVersion.ComparisonResult.NEWER) {
                plugin.logger().warn(
                        "Before updating Treasury, ensure that your Economy Provider utilizes");
                plugin.logger().warn("the latest version of the Treasury Economy API.");
                plugin.logger().warn(" ");
            }
            plugin.logger().error(
                    "You must resolve this issue as soon as possible. Leaving this issue unresolved can");
            plugin.logger().error(
                    "cause errors with your Economy Provider, and therefore, have the potential to severely harm your server's economy");
        } else if (apiComparisonResult == PluginVersion.ComparisonResult.NEWER) {
            // this means that "providerVersion" is newer than the latest version
            plugin
                    .logger()
                    .error("Economy provider going by the plugin name '" + providerEconomyProvider
                            .registrar()
                            .getName() + "'" + "is utilising a newer version of the Treasury Economy API");
            plugin.logger().error("than what your current version of Treasury provides.");
            plugin.logger().error(" ");
            if (comparisonResult == PluginVersion.ComparisonResult.EQUAL) {
                if (!currentVersionDevelopment) {
                    plugin.logger().warn(
                            "Since you seem to be running the latest version of Treasury, please check if your");
                    plugin.logger().warn(
                            "Economy Provider expects you to run a development build of Treasury instead of a release build.");
                } else {
                    plugin.logger().warn(
                            "Check if there are any newer development builds available which have a newer Economy API version.");
                }
            } else if (comparisonResult == PluginVersion.ComparisonResult.NEWER) {
                plugin.logger().warn(
                        "As mentioned up, a Treasury plugin update is available for download - this may resolve");
                plugin.logger().warn("the mismatching API versions.");
            } else if (comparisonResult == PluginVersion.ComparisonResult.OLDER) {
                plugin.logger().warn(
                        "Check if there are any newer development builds available which have a newer Economy API version.");
            }
            plugin.logger().error(" ");
            plugin.logger().error(
                    "You must resolve this issue as soon as possible. Leaving this issue unresolved can");
            plugin.logger().error(
                    "cause errors with your Economy Provider, and therefore, have the potential to severely harm your server's economy");
        }
    }

}
