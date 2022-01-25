/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.utils.downloader;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a platform, from where it is able to download a jar.
 *
 * @author MrIvanPlays
 */
public enum DownloadPlatform {
    CODEMC("https://ci.codemc.io/job/lokka30/job/Treasury"),
    MRIVANPLAYS("https://ci.mrivanplays.com/job/Treasury");

    private static final String BUILD_URL = "/api/json?tree=builds%5Bnumber,url,result,artifacts%5BrelativePath%5D,changeSet%5Bitems%5Bdate%5D%5D%5D%7B,1%7D";
    private static final String DOWNLOAD_URL_BASE = "/%number%/artifact/";

    private final String buildUrl;
    private final String downloadBase;

    DownloadPlatform(@NotNull String baseUrl) {
        this.buildUrl = baseUrl + BUILD_URL;
        this.downloadBase = baseUrl + DOWNLOAD_URL_BASE;
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


}
