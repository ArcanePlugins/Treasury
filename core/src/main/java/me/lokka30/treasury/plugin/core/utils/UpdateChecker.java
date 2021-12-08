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

package me.lokka30.treasury.plugin.core.utils;

import com.google.gson.JsonObject;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;

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
