package me.lokka30.treasury.plugin.core.config;

public enum ConfigVersion {
    V1;

    private static ConfigVersion latest;

    public static ConfigVersion getLatest() {
        if (latest == null) {
            ConfigVersion[] versions = values();
            latest = versions[versions.length - 1];
        }
        return latest;
    }
}
