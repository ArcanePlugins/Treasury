package me.lokka30.treasury.plugin.core.utils;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
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
                    if (!plugin.getVersion().equalsIgnoreCase(latestVersion)) {
                        plugin.logger().warn(
                                "A new Treasury update is available - '&bv"
                                        + latestVersion
                                        + "&7' - please update as soon as possible."
                                        + " &8(&7You're running '&bv" + plugin.getVersion() + "&7'&8)"
                        );
                    }
                }
            } catch (IOException e) {
                plugin.logger().error("IO whilst trying to request Spigot", e);
            }
        });
    }
}
