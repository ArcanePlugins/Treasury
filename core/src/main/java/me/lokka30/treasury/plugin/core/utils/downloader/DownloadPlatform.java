/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.utils.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.function.Supplier;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a platform, from where it is able to download a jar.
 *
 * @author MrIvanPlays
 */
public enum DownloadPlatform {
    CODEMC("https://ci.codemc.io/job/lokka30/job/Treasury", "changeSet"),
    MRIVANPLAYS(() -> {
        try {
            String releaseChannel;
            File pluginFile = new File(DownloadPlatform.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());
            if (TreasuryPlugin.getInstance().validatePluginJar(pluginFile)) {
                JarFile jar = new JarFile(pluginFile);
                Manifest manifest = jar.getManifest();
                releaseChannel = manifest.getMainAttributes().getValue("releaseChannel");
            } else {
                TreasuryPlugin
                        .getInstance()
                        .logger()
                        .warn("Couldn't validate plugin jar, falling back to master release channel");
                releaseChannel = "master";
            }
            return "https://ci.mrivanplays.com/job/Treasury/job/" + URLEncoder.encode(releaseChannel,
                    "UTF-8"
            );
        } catch (IOException e) {
            throw new RuntimeException("IO whilst trying to get jar file", e);
        } catch (URISyntaxException e) {
            // even though impossible
            throw new RuntimeException(e);
        }
    }, "changeSets");

    private static final String BUILD_URL = "/api/json?tree=builds%5Bnumber,url,result,artifacts%5BrelativePath%5D,changeSet%5Bitems%5Bdate%5D%5D%5D%7B,1%7D";
    private static final String DOWNLOAD_URL_BASE = "/%number%/artifact/";

    private final String buildUrl;
    private final String downloadBase;
    private final String changeSetWord;

    DownloadPlatform(@NotNull String baseUrl, @NotNull String changeSetWord) {
        this(() -> baseUrl, changeSetWord);
    }

    DownloadPlatform(@NotNull Supplier<String> baseUrlSupplier, @NotNull String changeSetWord) {
        String baseUrl = baseUrlSupplier.get();
        this.buildUrl = baseUrl + BUILD_URL.replace("changeSet", changeSetWord);
        this.downloadBase = baseUrl + DOWNLOAD_URL_BASE;
        this.changeSetWord = changeSetWord;
    }

    /**
     * Returns the URL from which information is retrieved for the latest build
     *
     * @return build information url
     */
    @NotNull
    public String buildUrl() {
        return buildUrl;
    }

    /**
     * Returns the URL base of the jenkins download url.
     *
     * @return download base url
     */
    @NotNull
    public String downloadBase() {
        return downloadBase;
    }

    /**
     * Used in build url formation and json parsing, the "changeSet" word changes based whether
     * the jenkins job is set up as a free style project, or a multibranch pipeline, hence - the
     * need of this.
     *
     * @return "changeSet" or "changeSetS" depending on the platform
     */
    @NotNull
    public String changeSetWord() {
        return this.changeSetWord;
    }


}
