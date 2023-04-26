/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.utils.downloader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.utils.Utils;
import org.jetbrains.annotations.Nullable;

/**
 * Something which downloads jars
 *
 * @author MrIvanPlays
 */
public final class PluginDownloader {

    /**
     * Downloads the latest Treasury plugin jar.
     *
     * @param source source to send messages to, which could be null, and if null, errors will be
     *               sent to the console.
     * @return success state - true if everything gone good, false otherwise
     */
    public static boolean downloadLatest(@Nullable CommandSource source) {
        TreasuryPlugin plugin = TreasuryPlugin.getInstance();
        if (source == null) {
            plugin.logger().info("A latest Treasury plugin download has been triggered.");
        }
        DownloadPlatform downloadPlatform = plugin
                .configAdapter()
                .getSettings()
                .getDownloadPlatform();
        String platformName = plugin.platform().specificationName();
        try {
            // first get the latest build
            URL latestBuild = new URL(downloadPlatform.buildUrl());
            try (Reader in = new InputStreamReader(latestBuild.openStream())) {
                JsonObject object = Utils.GSON.fromJson(in, JsonObject.class);
                JsonObject build = object.getAsJsonArray("builds").get(0).getAsJsonObject();

                // check dates
                OffsetDateTime currentJarDate = OffsetDateTime.parse(PluginDownloader.class
                        .getPackage()
                        .getImplementationVersion());
                OffsetDateTime buildDate = parseDate(build
                        .getAsJsonObject("changeSet")
                        .getAsJsonArray("items")
                        .get(0)
                        .getAsJsonObject()
                        .get("date")
                        .getAsString());
                if (currentJarDate.isEqual(buildDate) || currentJarDate.isAfter(buildDate)) {
                    if (source != null) {
                        source.sendMessage(Message.of(MessageKey.DOWNLOAD_LATEST_ALREADY_LATEST));
                    } else {
                        plugin.logger().warn("Already running latest.");
                    }
                    return false;
                }

                String downloadUrlString = downloadPlatform.downloadBase();
                downloadUrlString = downloadUrlString.replace("%number%",
                        Integer.toString(build.get("number").getAsInt())
                );

                String downloadPath = null;
                for (JsonElement element : build.getAsJsonArray("artifacts")) {
                    JsonObject artifact = element.getAsJsonObject();
                    String relativePath = artifact.get("relativePath").getAsString();
                    if (relativePath.contains(platformName) && !relativePath.contains("-sources")) {
                        downloadPath = relativePath;
                        break;
                    }
                }
                if (downloadPath == null) {
                    // nag the user to nag us that something went wrong
                    if (source != null) {
                        source.sendMessage(Message.of(MessageKey.DOWNLOAD_LATEST_COULDNT_PARSE));
                    } else {
                        plugin.logger().warn(
                                "Something went wrong whilst parsing latest build download data. Please notify a Treasury developer ASAP.");
                    }
                    return false;
                }
                downloadUrlString = downloadUrlString.concat(downloadPath);

                // get old jar before download just in case something goes wrong
                File oldJar = new File(PluginDownloader.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI());

                // prepare for a download
                String[] downloadPathParts = downloadPath.split("/");
                String fileName = downloadPathParts[downloadPathParts.length - 1];
                File file = new File(plugin.pluginsFolder().toFile(), fileName);

                // now download it
                URL downloadUrl = new URL(downloadUrlString);
                try (InputStream is = downloadUrl.openStream()) {
                    file.createNewFile();
                    try (OutputStream out = new FileOutputStream(file)) {
                        out.write(readAllBytes(is));
                    }
                }

                // check if the old jar we found is valid and if it is valid - delete it on exit
                if (plugin.validatePluginJar(oldJar)) {
                    oldJar.deleteOnExit();
                }
                return true;
            }
        } catch (Throwable e) {
            if (source != null) {
                source.sendMessage(Message.of(MessageKey.DOWNLOAD_LATEST_ERROR));
            }
            plugin.logger().error("Unable to retrieve latest build information", e);
            return false;
        }
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss Z");

    private static OffsetDateTime parseDate(String date) {
        OffsetDateTime parsed = OffsetDateTime.parse(date, DATE_TIME_FORMATTER);
        return parsed.withOffsetSameInstant(ZoneOffset.UTC);
    }

    // InputStream#readAllBytes, but ported to java 8
    private static byte[] readAllBytes(InputStream is) throws IOException {
        List<byte[]> bufs = null;
        byte[] result = null;
        int total = 0;
        int remaining = Integer.MAX_VALUE;
        int n;
        do {
            byte[] buf = new byte[Math.min(remaining, 8192)];
            int nread = 0;

            while ((n = is.read(buf, nread, Math.min(buf.length - nread, remaining))) > 0) {
                nread += n;
                remaining -= n;
            }

            if (nread > 0) {
                if ((Integer.MAX_VALUE - 8) - total < nread) {
                    throw new OutOfMemoryError("Required array size too large");
                }
                if (nread < buf.length) {
                    buf = Arrays.copyOfRange(buf, 0, nread);
                }
                total += nread;
                if (result == null) {
                    result = buf;
                } else {
                    if (bufs == null) {
                        bufs = new ArrayList<>();
                        bufs.add(result);
                    }
                    bufs.add(buf);
                }
            }
        } while (n >= 0 && remaining > 0);

        if (bufs == null) {
            if (result == null) {
                return new byte[0];
            }
            return result.length == total ? result : Arrays.copyOf(result, total);
        }

        result = new byte[total];
        int offset = 0;
        remaining = total;
        for (byte[] b : bufs) {
            int count = Math.min(b.length, remaining);
            System.arraycopy(b, 0, result, offset, count);
            offset += count;
            remaining -= count;
        }

        return result;
    }

}
