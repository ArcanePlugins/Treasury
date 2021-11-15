package me.lokka30.treasury.plugin.core.config;

/**
 * Represents a config version in order to track generated entries.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public enum ConfigVersion {
    V1;

    private static ConfigVersion latest;

    /**
     * Returns the latest config version known.
     *
     * @return latest config version
     */
    public static ConfigVersion getLatest() {
        if (latest == null) {
            ConfigVersion[] versions = values();
            latest = versions[versions.length - 1];
        }
        return latest;
    }
}
