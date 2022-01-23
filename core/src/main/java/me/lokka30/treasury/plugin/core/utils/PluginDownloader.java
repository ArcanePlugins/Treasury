/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.utils;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import org.jetbrains.annotations.Nullable;

/**
 * Something which downloads jars
 *
 * @author MrIvanPlays
 */
public final class PluginDownloader {

    private static final String LATEST_BUILD = "https://ci.mrivanplays.com/job/Treasury/api/json?tree=builds%5Bnumber,url,result,artifacts%5BrelativePath%5D,changeSet%5Bitems%5BcommitId,date%5D%5D%5D%7B,1%7D";
    private static final String DOWNLOAD_URL = "https://ci.mrivanplays.com/job/Treasury/%number%/artifact/";

    public static void downloadLatest(@Nullable CommandSource source) {
        TreasuryPlugin plugin = TreasuryPlugin.getInstance();
        String platformName = plugin.getPlatform().name().toLowerCase(Locale.ROOT);
        try {
            // first get the latest build
            URL latestBuild = new URL(LATEST_BUILD);
            try (Reader in = new InputStreamReader(latestBuild.openStream())) {
                JsonObject object = Utils.GSON.fromJson(in, JsonObject.class);
                JsonObject build = object.getAsJsonArray("builds").get(0).getAsJsonObject();
                String downloadUrlCopy = DOWNLOAD_URL;
                downloadUrlCopy = downloadUrlCopy.replace("%number%",
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
                    plugin.logger().warn(
                            "Something went wrong whilst parsing latest build download data. Please notify a Treasury developer ASAP.");
                    // and return
                    return;
                }
                downloadUrlCopy = downloadUrlCopy.concat(downloadPath);

                // find old jar because after we download we may not be able to find it
                File oldJar = null;
                for (File file : plugin
                        .getPluginsFolder()
                        .toFile()
                        .listFiles((dir, name) -> name.contains("treasury"))) {
                    if (file != null) {
                        // first not null we find should be the jar
                        oldJar = file;
                        break;
                    }
                }

                // prepare for a download
                String[] downloadPathParts = downloadPath.split("/");
                String fileName = downloadPathParts[downloadPathParts.length - 1];
                File file = new File(plugin.getPluginsFolder().toFile(), fileName);

                // now download it
                URL downloadUrl = new URL(URLEncoder.encode(downloadUrlCopy, "UTF-8"));
                try (InputStream is = downloadUrl.openStream()) {
                    file.createNewFile();
                    try (OutputStream out = new FileOutputStream(file)) {
                        out.write(readAllBytes(is));
                    }
                }

                // delete the old jar on exit (if we found it)
                if (oldJar != null) {
                    oldJar.deleteOnExit();
                }
            }
        } catch (IOException e) {
            plugin.logger().error("Unable to retrieve latest build information", e);
        }
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
