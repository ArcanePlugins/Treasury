package me.lokka30.treasury.plugin.core.utils;

import java.util.Objects;
import me.lokka30.treasury.plugin.core.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * A version object, representing Treasury plugin's version.
 *
 * @author MrIvanPlays
 */
public final class PluginVersion {

    private final short majorRevision, minorRevision, patchRevision;
    private final boolean developmentVersion;
    private final String stringVersion;

    public PluginVersion(@NotNull String input, @NotNull Logger logger) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(logger, "logger");
        this.stringVersion = input;
        if (input.endsWith("-SNAPSHOT")) {
            developmentVersion = true;
            input = input.replace("-SNAPSHOT", "");
        } else {
            developmentVersion = false;
        }
        if (input.indexOf('.') == -1) {
            throw new IllegalArgumentException("Illegal Treasury version found! " + "If this is a live environment version or you're a developer doing changes, please fix this ASAP!");
        }
        String[] parts = input.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Illegal Treasury version found! " + "If this is a live environment version or you're a developer doing changes, please fix this ASAP!");
        }
        this.majorRevision = Short.parseShort(parts[0]);
        this.minorRevision = Short.parseShort(parts[1]);
        this.patchRevision = Short.parseShort(parts[2]);
    }

    /**
     * Get the major revision of this plugin version.
     *
     * @return major version
     */
    public short getMajorRevision() {
        return majorRevision;
    }

    /**
     * Get the minor revision of this plugin version.
     *
     * @return minor version
     */
    public short getMinorRevision() {
        return minorRevision;
    }

    /**
     * Get the patch revision of this plugin version.
     *
     * @return patch version
     */
    public short getPatchRevision() {
        return patchRevision;
    }

    /**
     * Returns whether this plugin version is a development version.
     *
     * @return development version or not
     */
    public boolean isDevelopmentVersion() {
        return developmentVersion;
    }

    /**
     * Compares the specified {@link PluginVersion} {@code other} to this plugin version.
     *
     * @param other version to compare to
     * @return comparison result
     */
    public ComparisonResult compare(PluginVersion other) {
        if (other.getMajorRevision() > majorRevision) {
            return ComparisonResult.NEWER;
        } else if (other.getMajorRevision() < majorRevision) {
            return ComparisonResult.OLDER;
        } else {
            if (other.getMinorRevision() > minorRevision) {
                return ComparisonResult.NEWER;
            } else if (other.getMinorRevision() < minorRevision) {
                return ComparisonResult.OLDER;
            } else {
                if (other.getPatchRevision() > patchRevision) {
                    return ComparisonResult.NEWER;
                } else if (other.getPatchRevision() < patchRevision) {
                    return ComparisonResult.OLDER;
                } else {
                    if (other.isDevelopmentVersion() && developmentVersion) {
                        // we're safe to return equal and call it a day since we're warning the user
                        // that this is a development version and there might be a new one
                        return ComparisonResult.EQUAL;
                    } else if (other.isDevelopmentVersion()) {
                        // assume it's older since if this is not a development version it means it is a release
                        // and releases are newer than development versions
                        return ComparisonResult.OLDER;
                    } else if (developmentVersion) {
                        // assume it's newer since if the other version is not a development version it means it is
                        // a release and releases are newer than development versions
                        return ComparisonResult.NEWER;
                    } else {
                        return ComparisonResult.EQUAL;
                    }
                }
            }
        }
    }

    /**
     * Represents a result of comparing two {@link PluginVersion PluginVersions}.
     *
     * @author MrIvanPlays
     */
    public enum ComparisonResult {

        /**
         * The version which was given to compare to is older than the version comparing.
         */
        OLDER,

        /**
         * The version which was given to compare to is newer than the version comparing.
         */
        NEWER,

        /**
         * The version which was given to compare to is the same as the version comparing.
         */
        EQUAL
    }

    @Override
    public String toString() {
        return stringVersion;
    }

}
