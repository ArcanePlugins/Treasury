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
    private final boolean developmentVersion, release;
    private final String commit, stringVersion;

    public PluginVersion(@NotNull String input, @NotNull Logger logger) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(logger, "logger");
        this.stringVersion = input;

        if (input.indexOf('.') == -1 || input.indexOf('-') == -1) {
            throw new IllegalArgumentException(
                    "Illegal Treasury version found! If this is a live environment version or you're a developer doing changes, please fix this ASAP!");
        }
        // parse commit before version
        String[] bigParts = input.split("-");
        if (bigParts.length != 3) {
            throw new IllegalArgumentException(
                    "Illegal Treasury version found! If this is a live environment version or you're a developer doing changes, please fix this ASAP!");
        }
        this.commit = bigParts[1];

        this.developmentVersion = bigParts[2].equalsIgnoreCase("SNAPSHOT");
        this.release = bigParts[2].equalsIgnoreCase("RELEASE");

        String[] parts = bigParts[0].split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Illegal Treasury version found! If this is a live environment version or you're a developer doing changes, please fix this ASAP!");
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
     * Returns whether this plugin version is a release version.
     *
     * @return release version or not
     */
    public boolean isReleaseVersion() {
        return release;
    }

    /**
     * Returns the abbreviated commit id, stored inside the version.
     *
     * @return commit id
     */
    public String getCommit() {
        return commit;
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
                    // do not compare development versions - we do other stuff inside the update
                    // checker
                    return ComparisonResult.EQUAL;
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
        EQUAL,

        /**
         * Special case comparison result constant, which means the version which was given to
         * compare was something we couldn't understand somehow.
         */
        UNKNOWN
    }

    @Override
    public String toString() {
        return stringVersion;
    }

}
