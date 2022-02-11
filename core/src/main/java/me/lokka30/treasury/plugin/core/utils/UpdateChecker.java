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
import me.lokka30.treasury.plugin.core.TreasuryPlugin;

/**
 * Represents an update checker for spigot.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public final class UpdateChecker {

    private static final int RESOURCE_ID = 99531;

    private static final String SPIGOT_REQUEST_URI = String.format("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=%s",
            RESOURCE_ID
    );

    private static final String GITHUB_COMMITS_URI = "https://api.github.com/repos/lokka30/Treasury/commits";

    /**
     * Runs an update check.
     */
    public static void checkForUpdates() {
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
    }

}
